package com.mif50.instagram.ui.base.custom

import android.app.Dialog
import android.content.Context
import android.view.Window

import android.widget.ProgressBar
import android.widget.TextView
import com.mif50.instagram.R


class CustomProgress {
    private var mDialog: Dialog? = null

    fun showProgress(context: Context, message: String, cancelable: Boolean) {
        mDialog = Dialog(context)
        // no tile for the dialog
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.prograss_bar_dialog)
        mProgressBar = mDialog!!.findViewById(R.id.progress_bar) as ProgressBar
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        val progressText = mDialog!!.findViewById(R.id.progress_text) as TextView
        progressText.text = message
        // you can change or add this line according to your need
        mProgressBar?.isIndeterminate = true
        mDialog!!.setCancelable(true)
        mDialog!!.setCanceledOnTouchOutside(cancelable)
        mDialog!!.show()
    }

    fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    companion object {

        var customProgress: CustomProgress? = null
        var mProgressBar: ProgressBar? = null

        val instance: CustomProgress
            get() {
                if (customProgress == null) {
                    customProgress =
                        CustomProgress()
                }
                return customProgress!!
            }
    }
}