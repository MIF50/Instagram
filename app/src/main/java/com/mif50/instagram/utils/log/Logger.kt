package com.mif50.instagram.utils.log

import android.annotation.SuppressLint
import android.util.Log
import com.mif50.instagram.BuildConfig
import timber.log.Timber

object Logger {

    private const val TAG ="inst.debug"

    init {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    fun d(tag: String, s: String, vararg params: Any) =
        Timber.tag(tag).d(s, params)

    fun d(tag: String, throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(tag).d(throwable, s, params)

    fun i(tag: String, s: String, vararg params: Any) =
        Timber.tag(tag).i(s, params)

    fun i(tag: String, throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(tag).i(throwable, s, params)

    fun w(tag: String, s: String, vararg params: Any) =
        Timber.tag(tag).w(s, params)

    fun w(tag: String, throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(tag).w(throwable, s, params)

    fun e(tag: String, s: String, vararg params: Any) =
        Timber.tag(tag).e(s, params)

    fun e(tag: String, throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(tag).e(throwable, s, params)


    // =================================================================



    fun d(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(TAG).d(throwable, s, params)

    fun i(s: String, vararg params: Any) =
        Timber.tag(TAG).i(s, params)

    fun i(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(TAG).i(throwable, s, params)

    fun w(s: String, vararg params: Any) =
        Timber.tag(TAG).w(s, params)

    fun w(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(TAG).w(throwable, s, params)

    fun e(s: String, vararg params: Any) =
        Timber.tag(TAG).e(s, params)

    fun e(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag(TAG).e(throwable, s, params)

    // ===========================================================

   @SuppressLint("LogNotTimber")
   fun d(tag: String, message: String) {
       Log.d(TAG, "$tag:-    $message")

   }


}
