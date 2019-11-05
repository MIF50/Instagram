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

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    var posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    private val user: User = userRepository.getCurrentUser()!! // should not be used without login

    init {

        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle {pageIds ->
                    return@concatMapSingle  postRepository
                        .fetchHomePostList(pageIds.first, pageIds.second, user)
                        .subscribeOn(Schedulers.io())
                        .doOnError { handleNetworkError(it) }

                }
                .subscribe(
                    {
                        allPostList.addAll(it)
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
        val firstPostId = if (allPostList.isNotEmpty()) allPostList[0].id else null
        val lastPostId = if (allPostList.count() > 1) allPostList[allPostList.count() -1].id else null
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstPostId,lastPostId))
    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }
}