package com.mif50.instagram.di.component

import com.mif50.instagram.di.ViewModelScope
import com.mif50.instagram.di.module.ViewHolderModule
import com.mif50.instagram.ui.dummies.DummyItemViewHolder
import com.mif50.instagram.ui.home.posts.PostItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder: DummyItemViewHolder)

    fun inject(viewHolder: PostItemViewHolder)
}