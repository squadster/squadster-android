package com.android.squadster.screenslogic.squads

import android.content.Context
import android.widget.ImageView
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.model.data.server.model.ResultApiCall
import com.android.squadster.model.data.server.model.SquadMember
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SquadsPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    val draftUserInfo: DraftUserInfo,
    private val queriesInteractor: QueriesInteractor
) : BasePresenter<SquadsView>() {

    override fun attachView(view: SquadsView?) {
        super.attachView(view)

        getSquads()
    }

    fun loadUserAvatar(context: Context, view: ImageView) {
        Glide.with(context)
            .load(draftUserInfo.currentUserInfo?.imageUrl)
            .circleCrop()
            .into(view)
    }

    fun goToProfile() {
        flowRouter.navigateTo(Screens.ProfileScreen)
    }

    fun onBackPressed() {
        flowRouter.exit()
    }

    fun getSquads() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.getSquads()

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        viewState.setSquads(result.data)
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun sendRequest(squadId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.createSquadRequest(squadId)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        if (result.data.createSquadRequest?.squad?.id != null) {
                            viewState.updateSquadInvitation(
                                result.data.createSquadRequest.squad.id,
                                result.data.createSquadRequest.id,
                                RequestStatus.SEND
                            )
                        }
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun cancelRequest(requestId: String?) {
        if (requestId == null) return

        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.deleteSquadRequest(requestId)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        if (result.data.deleteSquadRequest?.id != null) {
                            viewState.updateSquadInvitation(
                                result.data.deleteSquadRequest.squad?.id ?: "",
                                result.data.deleteSquadRequest.id,
                                RequestStatus.CANCEL
                            )
                        }
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun createSquad(squadNumber: String, classDay: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.createSquad(squadNumber, classDay)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        val currentUser = result.data.members?.find { member ->
                            member.user?.id == draftUserInfo.currentUserInfo?.id
                        } ?: return@withContext

                        val squadMember = SquadMember(
                            currentUser.id,
                            currentUser.role,
                            currentUser.queueNumber,
                            result.data
                        )
                        draftUserInfo.currentUserInfo?.squadMember = squadMember
                        goToUserSquad()
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    private fun goToUserSquad() {
        flowRouter.replaceScreen(Screens.UserSquadScreen)
    }
}