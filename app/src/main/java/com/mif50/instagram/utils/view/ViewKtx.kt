package com.mif50.instagram.utils.view

import android.view.View

fun View.toggleVisibility(flag: Boolean) {
    if (flag) this.visible() else this.gone()
}

fun View.toggleVisibility() {
    if (this.visibility == View.VISIBLE) this.gone() else this.visibility
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}