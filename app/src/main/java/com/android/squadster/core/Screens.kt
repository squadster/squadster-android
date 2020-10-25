package com.android.squadster.core

import com.maltamenu.pax.ui.main.MainFlowFragment
import com.android.squadster.ui.main.main.UserInfoFragment
import com.android.squadster.ui.main.squad.SquadFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object PaymentFlow : SupportAppScreen() {

        override fun getFragment() = MainFlowFragment()
    }

    object UserInfoScreen : SupportAppScreen() {

        override fun getFragment() = UserInfoFragment()
    }

    object SquadScreen : SupportAppScreen() {

        override fun getFragment() = SquadFragment()
    }
}