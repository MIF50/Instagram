package com.mif50.instagram.data.repository

import com.mif50.instagram.data.local.db.DatabaseService
import com.mif50.instagram.data.model.Post
import com.mif50.instagram.data.model.User
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.remote.request.PostLikeModifyRequest
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {


    fun fetchHomePostList(firstPostId: String?, lastPostId: String?, user: User): Single<List<Post>> {
        return  networkService
            .doHomePostListCall(
                firstPostId,
                lastPostId,
                userId = user.id,
                accessToken = user.accessToken
            )
            .map {
                it.data
            }
    }

    fun makeLikePost(post: Post, user: User): Single<Post> {
        return networkService
            .doPostLikeCall(
                PostLikeModifyRequest(post.id),
                user.id,
                user.accessToken
            )
            .map {
                post.likedBy?.apply {
                    this.find {postUser -> postUser.id == user.id  } ?: this.add(
                        Post.User(
                            user.id,
                            user.name,
                            user.profilePicUrl
                        )
                    )
                }
                return@map post
            }
    }

    fun makeUnlikePost(post: Post, user: User): Single<Post> {
        return networkService
            .doPostUnlikeCall(
                PostLikeModifyRequest(post.id),
                user.id,
                user.accessToken
            )
            .map {
                post.likedBy?.apply {
                    this.find { postUser-> postUser.id == user.id }?.let { this.remove(it) }
                }
                return@map post
            }
    }
}