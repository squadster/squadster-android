package com.android.squadster.core

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FlowRouter(
    private val appRouter: Router
) : Router() {

    fun finishFlow() {
        appRouter.exit()
    }
}