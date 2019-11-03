package com.mif50.instagram.utils.display

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mif50.instagram.R
import com.mif50.instagram.utils.view.MyDrawableCompat


object Toaster {
    fun show(context: Context, text: CharSequence) {
        val toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT)
        MyDrawableCompat.setColorFilter(toast.view.background,R.color.white)
        val textView = toast.view.findViewById(android.R.id.message) as TextView
        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        toast.show()
    }
}

