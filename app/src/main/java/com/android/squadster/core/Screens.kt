package com.android.squadster.core

import com.android.squadster.ui.MainFlowFragment
import com.android.squadster.ui.auth.AuthFragment
import com.android.squadster.ui.profile.ProfileFragment
import com.android.squadster.ui.squads.SquadsFragment
import com.android.squadster.ui.usersquad.UserSquadFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object MainFlow : SupportAppScreen() {

        override fun getFragment() = MainFlowFragment()
    }

    object AuthScreen : SupportAppScreen() {

        override fun getFragment() = AuthFragment()
    }

    object SquadsScreen : SupportAppScreen() {

        override fun getFragment() = SquadsFragment()
    }

    object ProfileScreen : SupportAppScreen() {

        override fun getFragment() = ProfileFragment()
    }

    object UserSquadScreen : SupportAppScreen() {

        override fun getFragment() = UserSquadFragment()
    }
}