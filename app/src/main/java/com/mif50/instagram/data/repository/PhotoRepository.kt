package com.mif50.instagram.data.repository

import com.mif50.instagram.data.model.User
import com.mif50.instagram.data.remote.NetworkService
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val networkService: NetworkService) {

    fun uploadPhoto(file: File, user: User): Single<String> {
        // MediaType.parse("image/*") replace to "image/*".toMediaTypeOrNull()
        return MultipartBody.Part.createFormData(
            "image", file.name, RequestBody.create("image/*".toMediaTypeOrNull(), file)
        ).run {
            return@run networkService.doImageUpload(this, user.id, user.accessToken)
                .map {
                    it.data.imageUrl
                }
        }
    }
}