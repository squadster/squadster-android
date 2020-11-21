package com.android.squadster.extension

import com.android.squadster.model.data.server.model.Squad
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