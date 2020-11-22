package com.android.squadster.extension

import com.android.squadster.model.data.server.model.*
import com.squadster.server.CreateSquadMutation
import com.squadster.server.GetCurrentUserQuery
import com.squadster.server.GetSquadsQuery
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {

    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun GetSquadsQuery.Squad.toSquad(): Squad {
    val members = members?.filterNotNull() ?: ArrayList()
    val requests = requests?.filterNotNull() ?: ArrayList()
    return Squad(
        id,
        squadNumber ?: "",
        linkInvitationsEnabled ?: false,
        ArrayList(members),
        ArrayList(requests)
    )
}

fun CreateSquadMutation.CreateSquad.toUserSquad(): UserSquad {
    val members = members?.filterNotNull() ?: ArrayList()
    val requests = requests?.filterNotNull() ?: ArrayList()
    return UserSquad(
        id,
        advertisment,
        classDay,
        squadNumber ?: "",
        linkInvitationsEnabled,
        hashId,
        ArrayList(requests.map { it.toRequest() }),
        ArrayList(members.map { it.toMember() })
    )
}

fun GetCurrentUserQuery.CurrentUser.toUserInfo(): UserInfo {
    return UserInfo(
        id,
        firstName,
        lastName,
        mobilePhone,
        faculty,
        university,
        imageUrl,
        smallImageUrl,
        vkUrl,
        birthDate,
        squadMember?.toSquadMember()
    )
}

fun GetCurrentUserQuery.SquadMember.toSquadMember(): SquadMember {
    return SquadMember(
        id,
        role,
        queueNumber,
        squad?.toUserSquad()
    )
}

fun GetCurrentUserQuery.Squad.toUserSquad(): UserSquad {
    val members = members?.filterNotNull() ?: ArrayList()
    val requests = requests?.filterNotNull() ?: ArrayList()
    return UserSquad(
        id,
        advertisment,
        classDay,
        squadNumber,
        linkInvitationsEnabled,
        hashId,
        ArrayList(requests.map { it.toRequest() }),
        ArrayList(members.map { it.toMember() })
    )
}


fun GetCurrentUserQuery.Member.toMember(): Member {
    return Member(
        id,
        role,
        queueNumber,
        user?.toUserMember()
    )
}

fun GetCurrentUserQuery.Request.toRequest(): Request {
    return Request(
        id,
        approvedAt,
        user?.toUser()
    )
}

fun GetCurrentUserQuery.User.toUser(): User {
    return User(
        id,
        firstName,
        lastName,
        faculty,
        smallImageUrl,
        vkUrl
    )
}

fun GetCurrentUserQuery.User1.toUserMember(): UserMember {
    return UserMember(
        id,
        firstName,
        lastName,
        mobilePhone,
        faculty,
        university,
        imageUrl,
        smallImageUrl,
        vkUrl,
        birthDate
    )
}


fun CreateSquadMutation.Member.toMember(): Member {
    return Member(
        id,
        role,
        queueNumber,
        user?.toUserMember()
    )
}

fun CreateSquadMutation.Request.toRequest(): Request {
    return Request(
        id,
        approvedAt,
        user?.toUser()
    )
}

fun CreateSquadMutation.User.toUser(): User {
    return User(
        id,
        firstName,
        lastName,
        faculty,
        smallImageUrl,
        vkUrl
    )
}

fun CreateSquadMutation.User1.toUserMember(): UserMember {
    return UserMember(
        id,
        firstName,
        lastName,
        mobilePhone,
        faculty,
        university,
        imageUrl,
        smallImageUrl,
        vkUrl,
        birthDate
    )
}