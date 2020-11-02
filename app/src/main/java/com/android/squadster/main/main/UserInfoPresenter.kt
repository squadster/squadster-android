package com.android.squadster.main.main

import android.util.Log
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.android.squadster.model.data.server.draftUserInfo.UserInfo
import com.android.squadster.model.system.resource.ResourceManager
import com.google.gson.Gson
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class UserInfoPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    private val draftUserInfo: DraftUserInfo,
    private val gson: Gson
) : BasePresenter<UserInfoView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    infix fun getAllInfoAboutUser(info: String) {
        val infoAboutUser = info.substring(info.indexOf("{"), info.lastIndexOf("}") + 1)
        val user = gson.fromJson(infoAboutUser, UserInfo::class.java)
        draftUserInfo.userInfo = user
    }
}