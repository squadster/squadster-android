package com.android.squadster.di.module

import com.android.squadster.BuildConfig
import com.android.squadster.di.provider.ApolloProvider
import com.android.squadster.di.provider.OkHttpClientProvider
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.interceptor.AuthInterceptor
import com.google.gson.Gson
import com.android.squadster.di.provider.GsonProvider
import okhttp3.OkHttpClient
import toothpick.config.Module

class ServerModule : Module() {

    init {
        bind(AuthInterceptor::class.java)
            .singleton()
        bind(OkHttpClient::class.java)
            .toProvider(OkHttpClientProvider::class.java)
            .providesSingleton()
        bind(Gson::class.java)
            .toProvider(GsonProvider::class.java)
            .providesSingleton()
        bind(String::class.java)
            .toInstance(BuildConfig.BASE_URL_SQUADSTER_QUERY)
        bind(ApolloProvider::class.java)
            .singleton()
        bind(QueriesInteractor::class.java)
            .singleton()
    }
}