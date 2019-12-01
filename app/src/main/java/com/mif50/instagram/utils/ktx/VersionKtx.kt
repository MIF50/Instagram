package com.mif50.instagram.utils.ktx

import android.os.Build

val moreOrEqualAndroid10 get()  = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q