package com.mif50.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InfoResponse (
    @Expose
    @SerializedName("data")
    val dataInfo: DataInfo,

    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("status")
    val status: Int,

    @Expose
    @SerializedName("statusCode")
    val statusCode: String
) {
    data class DataInfo(
        @Expose
        @SerializedName("id")
        val id: String,

        @Expose
        @SerializedName("name")
        val name: String,

        @Expose
        @SerializedName("profilePicUrl")
        val profilePicUrl: String,

        @Expose
        @SerializedName("tagline")
        val tagline: String
    )
}