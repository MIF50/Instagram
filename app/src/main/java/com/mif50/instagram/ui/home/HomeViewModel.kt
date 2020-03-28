package com.mif50.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.mif50.instagram.data.model.Post
import com.mif50.instagram.data.model.User
import com.mif50.instagram.data.repository.PostRepository
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.ui.base.BaseViewModel
import com.mif50.instagram.utils.common.Resource
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

class HomeViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val allPostList: ArrayList<Post>,
    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider,compositeDisposable, networkHelper) {
    // LiveData
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    var posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()


    var firstId: String? = null
    var lastId: String? = null

    private val user: User = userRepository.getCurrentUser()!! // should not be used without login

    init {
        //TODO: onBackPressureDrop()

        // when downStream not ready to receive element
        // onBackPressureDrop() to drop the element from the sequence

        //TODO: contactMap

        // whenever a new item is emitted by the source observable, it will unsubscribe to and stop mirroring the observable that was generated
        // form the previously emitted item, and begin only mirroring the current one
        // contactMap the same flatMap but
        // contactMap has one big flow it waits for itch observable to finish all the work unit next one is processed

        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle { pageIds ->
                    return@concatMapSingle  postRepository
                        .fetchHomePostList(pageIds.first, pageIds.second, user)
                        .subscribeOn(Schedulers.io())
                        .doOnError { handleNetworkError(it) }

                }
                .subscribe(
                    {
                        allPostList.addAll(it)

                        firstId = allPostList.maxBy { post -> post.createdAt.time }?.id
                        lastId = allPostList.minBy { post -> post.createdAt.time }?.id

                        loading.postValue(false)
                        posts.postValue(Resource.success(it))

                    },{
                        loading.postValue(false)
                        handleNetworkError(it)

                    }
                )
        )
    }


    override fun onCreate() {
        loadMorePosts()

    }

    private fun loadMorePosts() {
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstId, lastId))
    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }


    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        refreshPosts.postValue(Resource.success(mutableListOf<Post>().apply { addAll(allPostList) }))
    }
}