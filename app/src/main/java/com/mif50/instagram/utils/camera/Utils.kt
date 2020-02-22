package com.mif50.instagram.utils.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.*

/**
 * Created by janisharali on 28/07/16.
 */
object Utils {
    private const val TAG = "Utils"
    /**
     * @param context
     * @param dirName
     * @param fileName
     * @param fileType
     * @return
     */
    @JvmStatic
    fun createImageFile(
        context: Context,
        dirName: String,
        fileName: String,
        fileType: String
    ): File? {
        return try {
            val file = createDir(context, dirName)
            val image = File(file.absoluteFile.toString() + File.separator + fileName + fileType)
            if (!image.parentFile.exists()) {
                image.parentFile.mkdirs()
            }
            image.createNewFile()
            image
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * @param context
     * @param dirName
     * @return
     */
    fun createDir(context: Context, dirName: String): File {
        val file =
            File(context.filesDir.toString() + File.separator + dirName)
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }

    /**
     * @param file
     * @param requiredHeight
     * @return
     */
    @JvmStatic
    fun decodeFile(file: File?, requiredHeight: Int): Bitmap? {
        return try { // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(file), null, o)
            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= requiredHeight &&
                o.outHeight / scale / 2 >= requiredHeight
            ) {
                scale *= 2
            }
            // Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            BitmapFactory.decodeStream(FileInputStream(file), null, o2)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * @param bitmap
     * @param filePath
     * @param imageType
     * @param compression
     */
    @JvmStatic
    fun saveBitmap(
        bitmap: Bitmap,
        filePath: String?,
        imageType: String?,
        compression: Int
    ) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filePath)
            when (imageType) {
                "png", "PNG", ".png" -> bitmap.compress(Bitmap.CompressFormat.PNG, compression, out)
                "jpg", "JPG", ".jpg", "jpeg", "JPEG", ".jpeg" -> bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    compression,
                    out
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @param imagePath
     * @return
     */
    @JvmStatic
    fun getImageRotation(imagePath: String?): Int {
        try {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val exif = ExifInterface(imageFile.path)
                val rotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                return exifToDegrees(rotation)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * @param src
     * @param rotation
     * @return
     */
    @JvmStatic
    fun rotateBitmap(src: Bitmap, rotation: Int): Bitmap {
        val matrix = Matrix()
        if (rotation != 0) {
            matrix.preRotate(rotation.toFloat())
            return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        }
        return src
    }

    /**
     * @param exifOrientation
     * @return
     */
    private fun exifToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}