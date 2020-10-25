package com.android.squadster.main.main

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.android.squadster.model.interactor.userInfo.UserInfoInteractor
import com.android.squadster.model.system.resource.ResourceManager
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class UserInfoPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    private val userInfoInteractor: UserInfoInteractor,
    private val draftUserInfo: DraftUserInfo
) : BasePresenter<UserInfoView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    fun getInfoAboutVKAccount(url: String): Boolean {
        val info = url.split("&")

        if (!info[0].contains("access_token")) return false

        draftUserInfo.token = info[0].split("=")[1]
        draftUserInfo.userId = info[2].split("=")[1]
        draftUserInfo.email = info[3].split("=")[1]

        return true
    }

    fun getFullVKProfile() {
        userInfoInteractor
            .getFullVKProfile()
            .doOnSubscribe {
                viewState.showLoading()
            }
            .doAfterTerminate {
                viewState.hideLoading()
            }
            .subscribe(
                {
                    flowRouter.navigateTo(Screens.SquadScreen)
                },
                {
                    errorHandler.handleError(it) { message -> viewState.showErrorMessage(message) }
                }
            )
    }

    companion object {
        private const val MAX_NUMBER_OF_DIGITS = 8
        private const val NUMBER_OF_DIGITS_TO_DISPLAY = 4
        private const val DEFAULT_STRING_OF_DIGITS = "0000"
        private const val DOT_SYMBOL = "."
    }
}