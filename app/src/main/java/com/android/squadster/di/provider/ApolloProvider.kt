package com.android.squadster.di.provider

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import javax.inject.Inject

class ApolloProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val serverPath: String
) {

    val apolloClient: ApolloClient by lazy {
        ApolloClient.builder()
            .serverUrl(serverPath)
            .okHttpClient(okHttpClient)
            .build()
    }
}