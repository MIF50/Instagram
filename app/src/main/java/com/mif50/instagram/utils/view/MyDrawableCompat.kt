package com.mif50.instagram.utils.view

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import com.mif50.instagram.utils.ktx.moreOrEqualAndroid10

object MyDrawableCompat {
    fun setColorFilter(@NonNull drawable: Drawable, color: Int) {
        if (moreOrEqualAndroid10) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}