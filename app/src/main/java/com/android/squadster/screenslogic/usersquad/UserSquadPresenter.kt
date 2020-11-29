package com.android.squadster.screenslogic.usersquad

import com.android.squadster.R
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.system.resource.ResourceManager
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
    private val resourceManager: ResourceManager,
    val draftUserInfo: DraftUserInfo,
    private val queriesInteractor: QueriesInteractor
) : BasePresenter<UserSquadView>() {

    fun isCurrentUserCommander(): Boolean {
        var isCurrentUserCommander = false

        draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.forEach { member ->
            if (member.id == draftUserInfo.currentUserInfo?.id) {
                isCurrentUserCommander = true
                return@forEach
            }
        }

        return isCurrentUserCommander
    }

    fun getClassDay(): String {

        return when (draftUserInfo.currentUserInfo?.squadMember?.squad?.classDay?.toLowerCase(Locale.getDefault())) {
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

    fun onBackPressed() {
        flowRouter.finishFlow()
    }

    fun goToSquadSettings() {
        flowRouter.navigateTo(Screens.SquadSettings)
    }

    fun goToSquads() {
        flowRouter.navigateTo(Screens.SquadsScreen)
    }

    fun goToProfile() {
        flowRouter.navigateTo(Screens.ProfileScreen)
    }

    fun goToSquadsWithoutExit() {
        flowRouter.replaceScreen(Screens.SquadsScreen)
    }

    fun deleteMember(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.deleteMember(
                id,
                object : ResponseCallback<DeleteSquadMemberMutation.Data> {

                    override fun success(data: DeleteSquadMemberMutation.Data) {
                        if (data.deleteSquadMember?.id != null) {
                            val member = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.find {
                                it.id == data.deleteSquadMember.id
                            }
                            if (member != null) {
                                draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.remove(member)
                                viewState.deleteSquadMember(data.deleteSquadMember.id)
                            }
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
                            val member = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.find {
                                it.id == data.updateSquadMember.id
                            }
                            if (member != null) {
                                val index = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.indexOf(member)
                                member.queueNumber = data.updateSquadMember.queueNumber
                                member.role = data.updateSquadMember.role
                                if (index != null) {
                                    draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.set(index, member)
                                    viewState.updateSquadMemberRole(
                                        data.updateSquadMember.id,
                                        data.updateSquadMember.role.toString(),
                                        data.updateSquadMember.queueNumber ?: 0
                                    )
                                }
                            }
                        }
                    }

                    override fun error(error: String) {
                        viewState.showErrorMessage(error)
                    }
                })
        }
    }

    fun deleteSquad() {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            queriesInteractor.deleteSquad(id, object : ResponseCallback<Boolean> {

                override fun success(data: Boolean) {
                    draftUserInfo.currentUserInfo?.squadMember?.squad = null
                    viewState.deleteSquad()
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }
}