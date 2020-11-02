package com.android.squadster.model.data.server.draftUserInfo

import com.google.gson.annotations.SerializedName

class DraftUserInfo {

    var userInfo: UserInfo? = null
}

data class UserInfo(
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("auth_token")
    val token: String,
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("faculty")
    val faculty: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("mobile_phone")
    val mobilePhone: String,
    @SerializedName("small_image_url")
    val smallImageUrl: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("university")
    val university: String,
    @SerializedName("vk_url")
    val vkUrl: String,
)