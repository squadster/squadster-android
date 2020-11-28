package com.android.squadster.screenslogic.squads

import android.content.Context
import android.widget.ImageView
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.system.resource.ResourceManager
import moxy.InjectViewState
import javax.inject.Inject
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.*
import com.bumptech.glide.Glide
import com.squadster.server.CreateSquadRequestMutation
import com.squadster.server.DeleteSquadRequestMutation
import com.squadster.server.GetSquadsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@InjectViewState
class SquadsPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
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
        flowRouter.finishFlow()
    }

    fun goToUserSquad() {
        flowRouter.replaceScreen(Screens.UserSquadScreen)
    }

    fun getSquads() {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.getSquads(object : ResponseCallback<GetSquadsQuery.Data> {

                override fun success(data: GetSquadsQuery.Data) {
                    if (data.squads != null && data.squads.isNotEmpty()) {
                        viewState.setSquads(data.squads.filterNotNull())
                    } else {
                        viewState.showEmptyListOfSquads()
                    }
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun sendRequest(squadId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.createSquadRequest(
                squadId,
                object : ResponseCallback<CreateSquadRequestMutation.Data> {

                    override fun success(data: CreateSquadRequestMutation.Data) {
                        if (data.createSquadRequest?.squad?.id != null) {
                            viewState.updateSquadInvitation(
                                data.createSquadRequest.squad.id,
                                data.createSquadRequest.id,
                                RequestStatus.SEND
                            )
                        }
                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }

    fun cancelRequest(requestId: String?) {
        requestId?.let {
            GlobalScope.launch(Dispatchers.IO) {
                queriesInteractor.deleteSquadRequest(
                    requestId,
                    object : ResponseCallback<DeleteSquadRequestMutation.Data> {

                        override fun success(data: DeleteSquadRequestMutation.Data) {
                            if (data.deleteSquadRequest?.id != null) {
                                viewState.updateSquadInvitation(
                                    data.deleteSquadRequest.squad?.id ?: "",
                                    data.deleteSquadRequest.id,
                                    RequestStatus.CANCEL
                                )
                            }
                        }

                        override fun error(error: String) {
                            viewState.showErrorMessage(error)
                        }
                    })
            }
        }
    }

    fun createSquad(squadNumber: String, classDay: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.createSquad(
                squadNumber,
                classDay,
                object : ResponseCallback<UserSquad> {

                    override fun success(data: UserSquad) {
                        draftUserInfo.currentUserInfo?.squadMember?.squad = data
                        viewState.goToUserSquad()
                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }
}