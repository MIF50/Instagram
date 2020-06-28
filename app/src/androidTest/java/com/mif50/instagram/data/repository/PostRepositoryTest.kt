package com.mif50.instagram.data.repository

import com.mif50.instagram.data.model.Post
import com.mif50.instagram.data.model.User
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.remote.Networking
import com.mif50.instagram.data.remote.request.PostCreationRequest
import com.mif50.instagram.data.remote.request.PostLikeModifyRequest
import com.mif50.instagram.data.remote.response.GeneralResponse
import com.mif50.instagram.data.remote.response.PostCreationResponse
import com.mif50.instagram.data.remote.response.PostListResponse
import dagger.Reusable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class PostRepositoryTest {

    private val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

    private val responseError = Throwable("Error response")

    private val userPost = Post.User(
        "id",
        name = "",
        profilePicUrl = ""
    )

    private val post = Post(
        "postId",
        "",
        0,
        0,
        userPost,
        mutableListOf(),
        Date()
    )

    private val postLikeRequest = PostLikeModifyRequest(
        post.id
    )

    private val imageUrl = "imageUrl"
    private val imgWidth = 0
    private val imgHeight = 0

    private val postCreation = PostCreationResponse.PostData (
        user.id,imageUrl,imgWidth,imgHeight,Date()
    )

    private val postCreationRequest = PostCreationRequest(
        imageUrl,imgWidth,imgHeight)


    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var sut: PostRepository

    @Before
    fun setup() {
        Networking.API_KEY = "FAKE_API_KEY"
        sut = PostRepository(networkService)
    }

    @Test
    fun fetchHomePostList_requestDoHomePostListCall() {
        // Arrange
        success_fetchHomePostList()
        // Act
        sut.fetchHomePostList("firstPostId", "lastPostId", user)
        // Assert
        verify(networkService).doHomePostListCall(
            "firstPostId",
            "lastPostId",
            user.id,
            user.accessToken,
            Networking.API_KEY
        )
    }

    @Test
    fun fetchHomePostList_requestDoHomePostListCall_completed() {
        // Arrange
        success_fetchHomePostList()
        // Act
        val result = sut.fetchHomePostList("firstPostId", "lastPostId", user)
        // Assert
        result.test().assertComplete()
    }

    @Test
    fun fetchHomePostList_failure_returnError() {
        // Arrange
        failure_fetchHomePostList()
        // Act
        val result = sut.fetchHomePostList("firstPostId", "lastPostId", user)
        // Assert
        result.test().assertError(responseError)
    }

    @Test
    fun makeLikePost_requestDoPostLikeCall() {
        // Arrange
        success_makeLikePost()
        // Act
        sut.makeLikePost(post, user)
        // Assert
        verify(networkService).doPostLikeCall(
            postLikeRequest,
            user.id,
            user.accessToken
        )
    }

    @Test
    fun makeLikePost_requestDoPostLikeCall_completed() {
        // Arrange
        success_makeLikePost()
        // Act
        val result = sut.makeLikePost(post, user)
        // Assert
        result.test().assertComplete()
    }

    @Test
    fun makeLikePost_failure_returnError() {
        // Arrange
        failure_makeLikePost()
        // Act
        val result = sut.makeLikePost(post,user)
        // Assert
        result.test().assertError(responseError)
    }

    @Test
    fun makeUnlikePost_requestDoPostUnlikeCall(){
        // Arrange
        success_makeUnLikePost()
        // Act
        sut.makeUnlikePost(post,user)
        // Assert
        verify(networkService).doPostUnlikeCall(
            postLikeRequest,
            user.id,
            user.accessToken
        )

    }

    @Test
    fun makeUnlikePost_requestDoPostLikeCall_completed(){
        // Arrange
        success_makeUnLikePost()
        // Act
        val result = sut.makeUnlikePost(post, user)
        // Assert
        result.test().assertComplete()
    }

    @Test
    fun makeUnlikePost_failure_returnError(){
        // Arrange
        failure_makeUnlikePost()
        // Act
        val result = sut.makeUnlikePost(post,user)
        // Assert
        result.test().assertError(responseError)

    }

    @Test
    fun createPost_requestPostCreationRequest(){
        // Arrange
        success_createPost()
        // Act
        sut.createPost(imageUrl,imgWidth,imgHeight,user)
        // Assert
        verify(networkService).doPostCreationCall(postCreationRequest,user.id,user.accessToken)
    }

    @Test
    fun createPost_requestPostCreationRequest_completed(){
        // Arrange
        success_createPost()
        // Act
        val result = sut.createPost(imageUrl,imgWidth,imgHeight,user)
        // Assert
        result.test().assertComplete()
    }

    @Test
    fun createPost_failure_returnError(){
        // Arrange
        failure_createPost()
        // Act
        val result = sut.createPost(imageUrl,imgWidth,imgHeight,user)
        // Assert
        result.test().assertError(responseError)

    }

    // ----------------- helper method --------------------------------------------

    private fun success_fetchHomePostList() {
        val result = Single.just(PostListResponse("statusCode", "message", listOf()))
        `when`(
            networkService.doHomePostListCall(
                "firstPostId",
                "lastPostId",
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        ).thenReturn(result)
    }

    private fun failure_fetchHomePostList() {
        `when`(
            networkService.doHomePostListCall(
                "firstPostId",
                "lastPostId",
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        ).thenReturn(Single.error(responseError))
    }

    private fun success_makeLikePost() {
        val result = Single.just(GeneralResponse("statusCode", "message"))
        `when`(
            networkService.doPostLikeCall(
                postLikeRequest,
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        )
            .thenReturn(result)

    }

    private fun failure_makeLikePost() {
        `when`(
            networkService.doPostLikeCall(
                postLikeRequest,
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        ).thenReturn(Single.error(responseError))
    }

    private fun success_makeUnLikePost(){
        val result = Single.just(GeneralResponse("statusCode", "message"))
        `when`(
            networkService.doPostUnlikeCall(
                postLikeRequest,
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        )
            .thenReturn(result)
    }

    private fun failure_makeUnlikePost(){
        `when`(
            networkService.doPostUnlikeCall(
                postLikeRequest,
                user.id,
                user.accessToken,
                Networking.API_KEY
            )
        )
            .thenReturn(Single.error(responseError))
    }

    private fun success_createPost(){
        val result = Single.just(PostCreationResponse(
            "statusCode",200,"message",postCreation
        ))
        `when`(
            networkService.doPostCreationCall(
                postCreationRequest,
                user.id,
                user.accessToken
            )
        ).thenReturn(result)

    }

    private fun failure_createPost(){
        `when`(
            networkService.doPostCreationCall(
                postCreationRequest,
                user.id,
                user.accessToken
            )
        ).thenReturn(Single.error(responseError))
    }


}