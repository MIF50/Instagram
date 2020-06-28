package com.mif50.instagram.common

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

/**
 * Created by MIF50
 * 20/06/2020.
 */
inline fun <reified T> mock() = Mockito.mock(T::class.java)
inline fun <T> whenever(methodCall: T) : OngoingStubbing<T> = Mockito.`when`(methodCall)