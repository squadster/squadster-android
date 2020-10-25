package com.maltamenu.pax.di.provider

import com.google.gson.Gson
import com.android.squadster.di.ServerPath
import com.android.squadster.model.data.server.ServerApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ServerApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    @ServerPath private val serverPath: String
) : Provider<ServerApi> {

    override fun get(): ServerApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(serverPath)
            .build()
            .create(ServerApi::class.java)
}