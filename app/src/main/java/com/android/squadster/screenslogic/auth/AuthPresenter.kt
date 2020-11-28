package com.android.squadster.screenslogic.auth

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.Auth
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.data.server.model.UserInfo
import com.android.squadster.model.data.storage.Prefs
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val draftUserInfo: DraftUserInfo,
    private val gson: Gson,
    private val prefs: Prefs,
    private val queriesInteractor: QueriesInteractor
) : BasePresenter<AuthView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    infix fun getAllInfoAboutUser(info: String) {
        val infoAboutUser = info.substring(info.indexOf("{"), info.lastIndexOf("}") + 1)
        val auth = gson.fromJson(infoAboutUser, Auth::class.java)
        prefs.accessToken = auth.user.token
        getCurrentUserInfo()
    }

    private fun getCurrentUserInfo() {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.getCurrentUserInfo(object : ResponseCallback<UserInfo> {

                override fun success(data: UserInfo) {
                    draftUserInfo.currentUserInfo = data

                    if (data.squadMember?.squad == null) {
                        viewState.goToSquadsScreen()
                    } else {
                        viewState.goToUserSquadScreen()
                    }
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun goToSquadsScreen() {
        flowRouter.navigateTo(Screens.SquadsScreen)
    }

    fun goToUserSquadScreen() {
        flowRouter.navigateTo(Screens.UserSquadScreen)
    }
}