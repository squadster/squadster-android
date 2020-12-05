package com.android.squadster.app.presentation

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AppPresenter @Inject constructor(
    private val router: Router,
    private val errorHandler: ErrorHandler
) : BasePresenter<AppView>() {

    override fun onDestroy() {
        super.onDestroy()

        errorHandler.onDestroy()
    }

    fun onAppStarted() {
        router.newRootScreen(Screens.MainFlow)
    }

    fun onBackPressed() {
        router.exit()
    }
}
