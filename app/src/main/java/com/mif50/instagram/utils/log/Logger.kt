package com.mif50.instagram.utils.log

import com.mif50.instagram.BuildConfig
import timber.log.Timber

object Logger {

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

    fun d(s: String, vararg params: Any) =
        Timber.tag("MIF").d(s, params)

    fun d(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag("MIF").d(throwable, s, params)

    fun i(s: String, vararg params: Any) =
        Timber.tag("MIF").i(s, params)

    fun i(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag("MIF").i(throwable, s, params)

    fun w(s: String, vararg params: Any) =
        Timber.tag("MIF").w(s, params)

    fun w(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag("MIF").w(throwable, s, params)

    fun e(s: String, vararg params: Any) =
        Timber.tag("MIF").e(s, params)

    fun e(throwable: Throwable, s: String, vararg params: Any) =
        Timber.tag("MIF").e(throwable, s, params)

}
