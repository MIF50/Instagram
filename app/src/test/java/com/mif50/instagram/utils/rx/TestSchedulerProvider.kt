package com.mif50.instagram.utils.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import java.nio.channels.Pipe

class TestSchedulerProvider(private val testScheduler: TestScheduler): SchedulerProvider{
    override fun computation(): Scheduler = testScheduler
    override fun io(): Scheduler = testScheduler
    override fun ui(): Scheduler = testScheduler
}