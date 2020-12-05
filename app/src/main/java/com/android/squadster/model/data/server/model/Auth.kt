package com.android.squadster.model.data.server.model

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("user")
    val user: Token
)

data class Token(
    @SerializedName("auth_token")
    val token: String
)