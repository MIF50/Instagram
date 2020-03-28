package com.mif50.instagram.ui.home.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mif50.instagram.R
import com.mif50.instagram.data.model.Image
import com.mif50.instagram.data.model.Post
import com.mif50.instagram.data.remote.Networking
import com.mif50.instagram.data.repository.PostRepository
import com.mif50.instagram.data.repository.UserRepository
import com.mif50.instagram.ui.base.adapter.BaseItemViewModel
import com.mif50.instagram.utils.common.Resource
import com.mif50.instagram.utils.common.TimeUtils
import com.mif50.instagram.utils.display.ScreenUtils
import com.mif50.instagram.utils.network.NetworkHelper
import com.mif50.instagram.utils.rx.SchedulerProvider

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository

): BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val  TAG = "PostItemViewModel"
    }

    override fun onCreate() {}

    private val user = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()

    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )
    // LiveData
    val nameLiveData: LiveData<String> = Transformations.map(data){ it.creator.name}
    val postTimeLiveData: LiveData<String> = Transformations.map(data) { TimeUtils.getTimeAgo(date = it.createdAt)}
    val likesCountLiveData: LiveData<Int> = Transformations.map(data) {it.likedBy?.size ?: 0}
    val isLikedLiveData: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { postUse -> postUse.id == user.id} !== null
    }

    val profileImageLiveData: LiveData<Image> = Transformations.map(data) {
        it.creator.profilePicUrl?.run {  Image(this,headers)}
    }

    val imageDetailLiveData: LiveData<Image> = Transformations.map(data) {
        Image(it.imageUrl, headers, screenWidth, calculateHeight(it))
    }

    private fun calculateHeight(post:Post): Int{
        return post.imageHeight?.let { height ->
            return@let (calculateScaleFactor(post) *  height).toInt()
        } ?: screenHeight / 3
    }

    private fun calculateScaleFactor(post: Post) = post.imageWidth?.let {imageWidth -> return@let screenWidth.toFloat() / imageWidth } ?: 1f

    fun onLikeClick() = data.value?.let {
        if (networkHelper.isNetworkConnected()) {
          callLikeOrUnLikeApi(it)
        } else {
            noInternetConnection()
        }
    }

    private fun callLikeOrUnLikeApi(post: Post){
        val api = if(isLikedLiveData.value == true) postRepository.makeUnlikePost(post, user) else postRepository.makeLikePost(post,user)

        compositeDisposable.add(
            api.subscribeOn(schedulerProvider.io())
                .subscribe(
                    { responsePost ->
                        if (responsePost.id == post.id) updateData(responsePost)
                    } , { error ->
                        handleNetworkError(error)
                    }
                )
        )
    }

    private fun noInternetConnection(){
        messageStringId.postValue(Resource.error((R.string.network_connection_error)))
    }
}