package com.android.squadster.screenslogic.usersquad

import android.content.Context
import android.widget.ImageView
import com.android.squadster.R
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.data.server.model.UserSquad
import com.android.squadster.model.system.resource.ResourceManager
import com.android.squadster.screenslogic.squads.SquadsView
import com.bumptech.glide.Glide
import com.squadster.server.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class UserSquadPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    val draftUserInfo: DraftUserInfo,
    private val queriesInteractor: QueriesInteractor
) : BasePresenter<UserSquadView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    fun isCurrentUserCommander(): Boolean {
        var isCurrentUserCommander = false

        draftUserInfo.userInfo?.squadMember?.squad?.members?.forEach { member ->
            if (member.id == draftUserInfo.userInfo?.id) {
                isCurrentUserCommander = true
                return@forEach
            }
        }

        return isCurrentUserCommander
    }

    fun getClassDay(): String {

        return when (draftUserInfo.userInfo?.squadMember?.squad?.classDay?.toLowerCase(Locale.getDefault())) {
            "monday" -> resourceManager.getString(R.string.monday)
            "tuesday" -> resourceManager.getString(R.string.tuesday)
            "wednesday" -> resourceManager.getString(R.string.wednesday)
            "thursday" -> resourceManager.getString(R.string.thursday)
            "friday" -> resourceManager.getString(R.string.friday)
            "saturday" -> resourceManager.getString(R.string.saturday)
            "sunday" -> resourceManager.getString(R.string.sunday)
            else -> resourceManager.getString(R.string.unknown_class_day)
        }
    }

    fun goToSquads() {
        flowRouter.navigateTo(Screens.SquadsScreen)
    }

    fun goToProfile() {
        flowRouter.navigateTo(Screens.ProfileScreen)
    }

    fun deleteMember(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.deleteMember(
                id,
                object : ResponseCallback<DeleteSquadMemberMutation.Data> {

                    override fun success(data: DeleteSquadMemberMutation.Data) {
                        if (data.deleteSquadMember?.id != null) {

                        }
                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }

    fun updateMemberRole(id: String, role: String, quequeNumber: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.updateMemberRole(
                id,
                role,
                quequeNumber,
                object : ResponseCallback<UpdateSquadMemberMutation.Data> {

                    override fun success(data: UpdateSquadMemberMutation.Data) {
                        if (data.updateSquadMember?.id != null) {

                        }
                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }

    /*fun loadUserAvatar(context: Context?, view: ImageView) {
        if (context != null) {
            Glide.with(context)
                .load(draftUserInfo.userInfo?.imageUrl)
                .circleCrop()
                .into(view)
        }
    }

    fun goToProfile() {
        flowRouter.navigateTo(Screens.ProfileScreen)
    }

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    fun getCurrentUserId(): String {
        return draftUserInfo.userInfo?.id ?: ""
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

                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }*/
}