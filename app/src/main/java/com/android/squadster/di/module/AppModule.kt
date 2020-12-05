package com.android.squadster.di.module

import android.content.Context
import com.android.squadster.app.App
import com.android.squadster.core.ErrorHandler
import com.android.squadster.model.system.resource.ResourceManager
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(
    app: App
) : Module() {

    init {
        bind(Context::class.java)
            .toInstance(app)
        bind(ResourceManager::class.java)
            .singleton()
        bind(ErrorHandler::class.java)
            .singleton()

        val cicerone = Cicerone.create()
        bind(Router::class.java)
            .toInstance(cicerone.router)
        bind(NavigatorHolder::class.java)
            .toInstance(cicerone.navigatorHolder)
    }
}