package com.mif50.instagram.ui.home.posts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.mif50.instagram.data.model.Post
import com.mif50.instagram.ui.base.adapter.BaseAdapter


class PostsAdapter (
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post, PostItemViewHolder>(parentLifecycle, posts) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= PostItemViewHolder(parent)
}