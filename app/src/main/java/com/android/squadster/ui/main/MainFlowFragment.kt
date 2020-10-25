package com.maltamenu.pax.ui.main

import android.os.Bundle
import com.android.squadster.core.FlowFragment
import com.android.squadster.core.Screens
import com.android.squadster.di.module.UserInfoModule
import com.android.squadster.extension.setLaunchScreen
import toothpick.Scope

class MainFlowFragment : FlowFragment() {

    override fun installScopeModules(scope: Scope) {
        super.installScopeModules(scope)

        scope.installModules(UserInfoModule())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.UserInfoScreen)
        }
    }
}