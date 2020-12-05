package com.android.squadster.model.data.server.interceptor

import com.android.squadster.model.data.storage.Prefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val prefs: Prefs
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER_TYPE_TOKEN ${prefs.accessToken}")
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER_TYPE_TOKEN = "Bearer"
    }
}