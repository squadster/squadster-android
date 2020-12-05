package com.android.squadster.model.data.server.model

sealed class ResultApiCall<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultApiCall<T>()

    data class Error(val code: Int, val message : String) : ResultApiCall<Nothing>()
}
