package com.android.squadster.di.provider

import com.android.squadster.di.ServerPath
import com.android.squadster.model.data.server.interceptor.AuthInterceptor
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import javax.inject.Inject

class ApolloProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @ServerPath private val serverPath: String
) {

    val apolloClient: ApolloClient by lazy {
        ApolloClient.builder()
            .serverUrl(serverPath)
            .okHttpClient(okHttpClient)
            .build()
    }
}