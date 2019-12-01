package com.mif50.instagram.ui.photo


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.mif50.instagram.R
import com.mif50.instagram.di.component.FragmentComponent
import com.mif50.instagram.ui.base.BaseFragment
import com.mif50.instagram.ui.main.MainSharedViewModel
import com.mif50.instagram.utils.camera.Camera
import com.mif50.instagram.utils.common.Event
import com.mif50.instagram.utils.common.LayoutRes
import com.mif50.instagram.utils.log.Logger
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.FileNotFoundException
import javax.inject.Inject

@LayoutRes(layout = R.layout.fragment_photo)
class PhotoFragment : BaseFragment<PhotoViewModel>() {

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    @Inject
    lateinit var camera: Camera

    companion object {
        const val TAG = "PhotoFragment"
        const val RESULT_GALLERY_IMG = 1001

        fun newInstance() = PhotoFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(this, Observer {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this))
                mainSharedViewModel.onHomeRedirect()
            }
        })
    }

    override fun setupView(view: View) {

        view_gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RESULT_GALLERY_IMG)
                }
        }

        view_camera.setOnClickListener {
            Logger.d(tag = TAG,message = "Camera view is tapped")
            try {
                Logger.d(tag = TAG,message = "try to take picture")
                camera.takePicture()
            } catch (e: Exception) {
                Logger.d(tag = TAG, message = "exception ${e.localizedMessage}")
                e.printStackTrace()
            }

        }

    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(reqCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (reqCode) {
                RESULT_GALLERY_IMG -> {
                    try {
                        intent?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.try_again)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.try_again)
                    }
                }
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.getCameraBitmapPath()!! }
                }
            }
        }
    }

}
