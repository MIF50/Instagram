package com.mif50.instagram.di.module

import androidx.lifecycle.LifecycleRegistry
import com.mif50.instagram.di.ViewModelScope
import com.mif50.instagram.ui.base.adapter.BaseItemViewHolder
import dagger.Module
import dagger.Provides

@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>) {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)
}