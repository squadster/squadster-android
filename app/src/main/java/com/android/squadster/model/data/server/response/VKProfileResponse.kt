package com.android.squadster.model.data.server.response

import com.google.gson.annotations.SerializedName

data class VKProfileResponse(
    @SerializedName("response")
    val response: List<Profile>
)

data class Profile(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String
)