package com.mif50.instagram.ui.dummy

import android.os.Bundle
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.dummies.DummiesFragment
import com.mif50.instagram.utils.common.LayoutRes

@LayoutRes(layout = R.layout.activity_dummy)
class DummyActivity : BaseActivity<DummyViewModel>() {

    companion object {
        const val TAG = "DummyActivity"
    }


    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        addDummiesFragment()
    }

    private fun addDummiesFragment() {
        supportFragmentManager.findFragmentByTag(DummiesFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, DummiesFragment.newInstance(), DummiesFragment.TAG)
            .commitAllowingStateLoss()
    }
}