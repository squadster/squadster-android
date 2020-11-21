package com.android.squadster.di.provider

import com.android.squadster.BuildConfig
import com.android.squadster.model.data.server.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
    private val authInterceptor: AuthInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return with(OkHttpClient.Builder()) {
            addInterceptor(authInterceptor)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addNetworkInterceptor(loggingInterceptor)
            }
            build()
        }
    }

    companion object {
        private const val TIMEOUT = 30L
    }
}