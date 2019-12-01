package com.mif50.instagram.utils.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mif50.instagram.BuildConfig
import com.mif50.instagram.utils.camera.Utils.createImageFile
import com.mif50.instagram.utils.camera.Utils.decodeFile
import com.mif50.instagram.utils.camera.Utils.getImageRotation
import com.mif50.instagram.utils.camera.Utils.rotateBitmap
import com.mif50.instagram.utils.camera.Utils.saveBitmap
import com.mif50.instagram.utils.log.Logger
import java.io.File
import java.io.IOException

/**
 * Created by ali on 4/11/15.
 */
class Camera private constructor(builder: Builder) {
    /**
     * Private variables
     */
    private val context: Context?
    private val activity: Activity?
    private val fragment: Fragment?
    private val compatFragment: Fragment?
    private var cameraBitmapPath: String? = null
    private var cameraBitmap: Bitmap? = null
    private val dirName: String
    private val imageName: String
    private val imageType: String
    private val imageHeight: Int
    private val compression: Int
    private val isCorrectOrientationRequired: Boolean
    private val mode: MODE?
    private val authority: String

    private fun setUpIntent(takePictureIntent: Intent) {
        Logger.d(TAG,"setUpIntent")
        var photoFile: File? = null
        try {
            photoFile = createImageFile(
                context!!,
                dirName,
                imageName,
                imageType
            )
        } catch (e: IOException) {
            Logger.d(TAG, "setUpIntent -> e = $e")
        }

        if (photoFile != null) {
            cameraBitmapPath = photoFile.absolutePath
            val uri = FileProvider.getUriForFile(context!!,authority, photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            val resInfoList = context.packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                Logger.d(TAG,"package name = $packageName")
                context.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

        } else {
            Logger.d(TAG, "setUpIntent -> Image file could not be created ")
            throw NullPointerException("Image file could not be created")
        }
    }

    /**
     * Initiate the existing camera apps
     *
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class, IllegalAccessException::class)
    fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        when (mode) {
            MODE.ACTIVITY -> if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
                Logger.d(TAG,"from activity try to open camera form ")
                setUpIntent(takePictureIntent)
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            } else {
                Logger.d(TAG, "from activity unable to open camera")
                throw IllegalAccessException("Unable to open camera")
            }
            MODE.FRAGMENT -> if (takePictureIntent.resolveActivity(fragment!!.activity!!.packageManager) != null) {
                Logger.d(TAG,"from fragment try to open camera form ")
                setUpIntent(takePictureIntent)
                fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            } else {
                Logger.d(TAG, "from fragment unable to open camera")
                throw IllegalAccessException("Unable to open camera")
            }
            MODE.COMPAT_FRAGMENT -> if (takePictureIntent.resolveActivity(compatFragment!!.activity!!.packageManager) != null) {
                Logger.d(TAG,"from compat fragment try to open camera form ")
                setUpIntent(takePictureIntent)
                compatFragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            } else {
                Logger.d(TAG, "from compat fragment unable to open camera")
                throw IllegalAccessException("Unable to open camera")
            }
        }
    }

    /**
     * @return the saved bitmap path but scaling bitmap as per builder
     */
    fun getCameraBitmapPath(): String? {
        val bitmap = getCameraBitmap()
        bitmap!!.recycle()
        return cameraBitmapPath
    }

    /**
     * @return The scaled bitmap as per builder
     */
    fun getCameraBitmap(): Bitmap? {
        return resizeAndGetCameraBitmap(imageHeight)
    }

    /**
     * @param imageHeight
     * @return Bitmap path with approx desired height
     */
    fun resizeAndGetCameraBitmapPath(imageHeight: Int): String? {
        val bitmap = resizeAndGetCameraBitmap(imageHeight)
        bitmap!!.recycle()
        return cameraBitmapPath
    }

    /**
     * @param imageHeight
     * @return Bitmap with approx desired height
     */
    fun resizeAndGetCameraBitmap(imageHeight: Int): Bitmap? {
        return try {
            if (cameraBitmap != null) {
                cameraBitmap!!.recycle()
            }
            cameraBitmap = decodeFile(
                File(cameraBitmapPath),
                imageHeight
            )
            if (cameraBitmap != null) {
                if (isCorrectOrientationRequired) {
                    cameraBitmap = rotateBitmap(
                        cameraBitmap!!,
                        getImageRotation(cameraBitmapPath)
                    )
                }
                saveBitmap(
                    cameraBitmap!!,
                    cameraBitmapPath,
                    imageType,
                    compression
                )
            }
            cameraBitmap
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Deletes the saved camera image
     */
    fun deleteImage() {
        if (cameraBitmapPath != null) {
            val image = File(cameraBitmapPath!!)
            if (image.exists()) {
                image.delete()
            }
        }
    }

    enum class MODE {
        ACTIVITY, FRAGMENT, COMPAT_FRAGMENT
    }

    /**
     * Camera builder declaration
     */
    class Builder {
         var context: Context? = null
         var activity: Activity? = null
         var fragment: Fragment? = null
         val compatFragment: Fragment? = null
         var dirName: String
         var imageName: String
         var imageType: String
         var imageHeight: Int
         var compression: Int
         var isCorrectOrientationRequired = false
         var mode: MODE? = null
         var REQUEST_TAKE_PHOTO: Int

        fun setDirectory(dirName: String?): Builder {
            if (dirName != null) this.dirName = dirName
            return this
        }

        fun setTakePhotoRequestCode(requestCode: Int): Builder {
            this.REQUEST_TAKE_PHOTO = requestCode
            return this
        }

        fun setName(imageName: String?): Builder {
            if (imageName != null) this.imageName = imageName
            return this
        }

        fun resetToCorrectOrientation(reset: Boolean): Builder {
            isCorrectOrientationRequired = reset
            return this
        }

        fun setImageFormat(imageFormat: String?): Builder {
            if (TextUtils.isEmpty(imageFormat)) {
                return this
            }
            when (imageFormat) {
                "png", "PNG", ".png" -> imageType =
                    IMAGE_FORMAT_PNG
                "jpeg", "JPEG", ".jpeg" -> imageType =
                    IMAGE_FORMAT_JPEG
                else -> imageType =
                    IMAGE_FORMAT_JPG
            }
            return this
        }

        fun setImageHeight(imageHeight: Int): Builder {
            this.imageHeight = imageHeight
            return this
        }

        fun setCompression(compression: Int): Builder {
            var compression = compression
            if (compression > 100) {
                compression = 100
            } else if (compression < 0) {
                compression = 0
            }
            this.compression = compression
            return this
        }

        fun build(activity: Activity): Camera {
            this.activity = activity
            context = activity.applicationContext
            mode = MODE.ACTIVITY
            return Camera(this)
        }

        fun build(fragment: Fragment): Camera {
            this.fragment = fragment
            context = fragment.activity!!.applicationContext
            mode = MODE.FRAGMENT
            return Camera(this)
        }

        init {
            dirName = IMAGE_DEFAULT_DIR
            imageName =
                IMAGE_DEFAULT_NAME + System.currentTimeMillis()
            imageHeight = IMAGE_HEIGHT
            compression = IMAGE_COMPRESSION
            imageType = IMAGE_FORMAT_JPG
            REQUEST_TAKE_PHOTO =
                Companion.REQUEST_TAKE_PHOTO
        }
    }

    companion object {
        private const val TAG = "Camera"


        const val IMAGE_JPG = "jpg"
        const val IMAGE_JPEG = "jpeg"
        const val IMAGE_PNG = "png"
        /**
         * default values used by camera
         */
        private const val IMAGE_FORMAT_JPG = ".jpg"
        private const val IMAGE_FORMAT_JPEG = ".jpeg"
        private const val IMAGE_FORMAT_PNG = ".png"
        private const val IMAGE_HEIGHT = 1000
        private const val IMAGE_COMPRESSION = 75
        private const val IMAGE_DEFAULT_DIR = "capture"
        private const val IMAGE_DEFAULT_NAME = "img_"
        /**
         * public variables to be used in the builder
         */
        var REQUEST_TAKE_PHOTO = 1234
    }

    /**
     * @param builder to copy all the values from.
     */
    init {
        activity = builder.activity
        context = builder.context
        mode = builder.mode
        fragment = builder.fragment
        compatFragment = builder.compatFragment
        dirName = builder.dirName
        REQUEST_TAKE_PHOTO =
            builder.REQUEST_TAKE_PHOTO
        imageName = builder.imageName
        imageType = builder.imageType
        isCorrectOrientationRequired = builder.isCorrectOrientationRequired
        compression = builder.compression
        imageHeight = builder.imageHeight
//        authority = context!!.applicationContext.packageName + ".imageprovider"
        authority = "${BuildConfig.APPLICATION_ID}.provider"

    }
}