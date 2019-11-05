package com.mif50.instagram.utils.dsl

inline infix fun <T> T.guard(block: T.() -> Unit): T{
    if (this == null) block()
    return this!!
}

inline infix fun <T> T.isNotNull(block: T.() -> Unit) {
    if (this != null){
        block()
    }
}

fun <T> T.isNotNull() : Boolean  = this != null



