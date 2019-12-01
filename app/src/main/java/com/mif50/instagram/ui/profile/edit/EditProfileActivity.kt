package com.mif50.instagram.ui.profile.edit

import android.os.Bundle
import com.mif50.instagram.R
import com.mif50.instagram.di.component.ActivityComponent
import com.mif50.instagram.ui.base.BaseActivity
import com.mif50.instagram.ui.base.CustomProgress
import com.mif50.instagram.utils.common.LayoutRes

@LayoutRes(layout = R.layout.activity_edit_profile)
class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    private val customProgress by lazy {
        CustomProgress.instance
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        customProgress.showProgress(this,"loading data from server ",false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        customProgress.hideProgress()
    }



}
