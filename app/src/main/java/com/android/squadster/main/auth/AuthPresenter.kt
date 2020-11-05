package com.android.squadster.main.auth

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.android.squadster.model.data.server.draftUserInfo.UserInfo
import com.android.squadster.model.system.resource.ResourceManager
import com.google.gson.Gson
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    private val draftUserInfo: DraftUserInfo,
    private val gson: Gson
) : BasePresenter<AuthView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    infix fun getAllInfoAboutUser(info: String) {
        val infoAboutUser = info.substring(info.indexOf("{"), info.lastIndexOf("}") + 1)
        val user = gson.fromJson(infoAboutUser, UserInfo::class.java)
        user.user.imageUrl = user.user.imageUrl.replace("&amp;", "&")
        draftUserInfo.userInfo = user
        goToSquadsScreen()
    }

    private fun goToSquadsScreen() {
        flowRouter.navigateTo(Screens.SquadsScreen)
    }
}