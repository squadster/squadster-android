package com.android.squadster.app

import android.app.Application
import com.android.squadster.BuildConfig
import com.android.squadster.di.DI
import com.android.squadster.di.module.AppModule
import com.android.squadster.di.module.ServerModule
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initDI()
    }

    private fun initDI() {
        val configuration = if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
        Toothpick.setConfiguration(configuration)

        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))
        Toothpick.openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule())
    }
}
