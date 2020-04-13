package com.mif50.instagram.di

import android.content.Context
import com.mif50.instagram.App
import com.mif50.instagram.di.component.ComponentTest
import com.mif50.instagram.di.component.DaggerComponentTest
import com.mif50.instagram.di.module.ApplicationModuleTest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ComponentRuleTest(private val context: Context): TestRule {

    private var componentTest: ComponentTest? = null

    fun getContext() = context

    private fun setupDaggerTestComponentInApplication(){
        val application = context.applicationContext as App
        componentTest = DaggerComponentTest.builder()
            .applicationModuleTest(ApplicationModuleTest(application))
            .build()
        application.setComponent(componentTest!!)
    }

    override fun apply(base: Statement, description: Description?): Statement {
        return object: Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                try {
                    setupDaggerTestComponentInApplication()
                    base.evaluate()
                } finally {
                    componentTest = null
                }
            }
        }
    }

}