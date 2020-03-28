package com.mif50.instagram.utils.view.rv

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnScrollListener(completion:(recyclerView: RecyclerView, dx:Int, dy:Int)->Unit){
    this.addOnScrollListener(object :RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            completion(recyclerView,dx,dy)
        }
    })
}



fun LinearLayoutManager.isLastItem():Boolean {
    return itemCount > 0 && itemCount == findLastVisibleItemPosition() + 1
}