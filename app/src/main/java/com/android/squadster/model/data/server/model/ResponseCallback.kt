package com.android.squadster.model.data.server.model

interface ResponseCallback<T> {

    fun success(data: T)

    fun error(error: String)
}