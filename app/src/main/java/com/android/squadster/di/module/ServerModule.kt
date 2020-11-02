package com.android.squadster.di.module

import com.android.squadster.BuildConfig
import com.android.squadster.di.ServerPath
import com.android.squadster.model.data.server.ServerApi
import com.google.gson.Gson
import com.maltamenu.pax.di.provider.GsonProvider
import com.android.squadster.di.provider.OkHttpClientProvider
import com.android.squadster.di.provider.ServerApiProvider
import okhttp3.OkHttpClient
import toothpick.config.Module

class ServerModule : Module() {

    init {
        // Network
        bind(OkHttpClient::class.java)
            .toProvider(OkHttpClientProvider::class.java)
            .providesSingleton()
        bind(Gson::class.java)
            .toProvider(GsonProvider::class.java)
            .providesSingleton()
        bind(String::class.java)
            .withName(ServerPath::class.java)
            .toInstance(BuildConfig.BASE_URL_VK)
        bind(ServerApi::class.java)
            .toProvider(ServerApiProvider::class.java)
            .providesSingleton()
    }
}