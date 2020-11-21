package com.android.squadster.screenslogic.auth

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.UserInfo
import com.android.squadster.model.data.storage.Prefs
import com.google.gson.Gson
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val draftUserInfo: DraftUserInfo,
    private val gson: Gson,
    private val prefs: Prefs
) : BasePresenter<AuthView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    infix fun getAllInfoAboutUser(info: String) {
        val infoAboutUser = info.substring(info.indexOf("{"), info.lastIndexOf("}") + 1)
        val user = gson.fromJson(infoAboutUser, UserInfo::class.java)
        user.user.imageUrl = user.user.imageUrl.replace("&amp;", "&")
        draftUserInfo.userInfo = user
        prefs.accessToken = draftUserInfo.userInfo?.user?.token
        prefs.currentUserId = draftUserInfo.userInfo?.user?.id
        goToSquadsScreen()
    }

    private fun goToSquadsScreen() {
        flowRouter.navigateTo(Screens.SquadsScreen)
    }
}