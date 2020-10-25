package com.android.squadster.model.data.server

import com.android.squadster.model.data.server.response.VKProfileResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

    @GET("users.get")
    fun getFullVKProfile(
        @Query("user_id") userId: String,
        @Query("v") version: String,
        @Query("access_token") token: String
    ): Single<VKProfileResponse>
}