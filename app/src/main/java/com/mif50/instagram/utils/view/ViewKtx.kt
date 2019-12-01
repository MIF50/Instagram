package com.mif50.instagram.utils.view

import android.view.View
import androidx.core.view.ViewCompat

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

fun View.setVisible(isVisible: Boolean, animate: Boolean = false, onEnd: (() -> Unit)? = null) {
    if (animate) {
        if (isVisible) {
            alpha = 0f
            setVisible(true)
        }
        ViewCompat.animate(this)
            .alpha(if (isVisible) 1f else 0f)
            .withEndAction {
                if (!isVisible) {
                    setVisible(false)
                }
                onEnd?.invoke()
            }
    } else {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}