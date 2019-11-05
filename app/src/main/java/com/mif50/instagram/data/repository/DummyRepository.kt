package com.mif50.instagram.data.repository

import com.mif50.instagram.data.local.db.DatabaseService
import com.mif50.instagram.data.model.Dummy
import com.mif50.instagram.data.remote.NetworkService
import com.mif50.instagram.data.remote.request.DummyRequest
import io.reactivex.Single
import javax.inject.Inject

class DummyRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {

    fun fetchDummy(id: String): Single<List<Dummy>> =
        networkService.doDummyCall(DummyRequest(id)).map { it.data }

}