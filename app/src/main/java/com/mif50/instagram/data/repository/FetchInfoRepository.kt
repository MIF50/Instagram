package com.mif50.instagram.data.repository

import com.mif50.instagram.data.model.User
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.remote.response.InfoResponse
import io.reactivex.Single
import javax.inject.Inject

class FetchInfoRepository @Inject constructor(
    private val networkService: NetworkService
) {

    fun doFetchInfo(user: User): Single<InfoResponse.DataInfo> {
        return networkService.doFetchInfoCall(
            userId = user.id,
            accessToken = user.accessToken
        )
            .map {
                return@map it.dataInfo
            }
    }

}