package com.android.squadster.model.data.server.model

import javax.inject.Inject

class DraftUserInfo @Inject constructor() {

    var currentUserInfo: UserInfo? = null

    var anotherUser: UserMember? = null
}

data class UserInfo(
    val id: String,
    val firstName: String,
    val lastName: String,
    val mobilePhone: String?,
    val faculty: String?,
    val university: String?,
    val imageUrl: String?,
    val smallImageUrl: String?,
    val vkUrl: String?,
    val birthDate: Any?,
    var squadMember: SquadMember?
)

data class SquadMember(
    val id: String,
    val role: Any?,
    val queueNumber: Int?,
    var squad: UserSquad?
)

data class UserSquad(
    val id: String,
    var advertisment: String?,
    var classDay: String?,
    var squadNumber: String?,
    var linkInvitationsEnabled: Boolean?,
    val hashId: String?,
    val requests: ArrayList<Request>?,
    val members: ArrayList<Member>?
)

data class Request(
    val id: String,
    val approvedAt: Any?,
    val user: User?
)

data class Member(
    val id: String,
    var role: Any?,
    var queueNumber: Int?,
    val user: UserMember?
)

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val faculty: String?,
    val smallImageUrl: String?,
    val vkUrl: String?
)

data class UserMember(
    val id: String,
    val firstName: String,
    val lastName: String,
    val mobilePhone: String?,
    val faculty: String?,
    val university: String?,
    val imageUrl: String?,
    val smallImageUrl: String?,
    val vkUrl: String?,
    val birthDate: Any?
)