package com.android.squadster.di.module

import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.android.squadster.model.interactor.userInfo.UserInfoInteractor
import toothpick.config.Module

class UserInfoModule : Module() {

    init {
        bind(DraftUserInfo::class.java)
            .toInstance(DraftUserInfo())
        bind(UserInfoInteractor::class.java)
            .singleton()
    }
}