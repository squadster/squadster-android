package com.android.squadster.screenslogic.squadsettings

import com.android.squadster.R
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.extension.toMember
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.system.resource.ResourceManager
import com.squadster.server.ApproveSquadRequestMutation
import com.squadster.server.DeleteSquadRequestMutation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject

@InjectViewState
class SquadSettingsPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val resourceManager: ResourceManager,
    val draftUserInfo: DraftUserInfo,
    private val queriesInteractor: QueriesInteractor
) : BasePresenter<SquadSettingsView>() {

    fun onBackPressed() {
        flowRouter.exit()
    }

    fun goToSquads() {
        flowRouter.replaceScreen(Screens.SquadsScreen)
    }

    fun getClassDay(): Pair<String, Int> {

        return when (draftUserInfo.currentUserInfo?.squadMember?.squad?.classDay?.toLowerCase(Locale.getDefault())) {
            "monday" -> resourceManager.getString(R.string.monday) to 0
            "tuesday" -> resourceManager.getString(R.string.tuesday) to 1
            "wednesday" -> resourceManager.getString(R.string.wednesday) to 2
            "thursday" -> resourceManager.getString(R.string.thursday) to 3
            "friday" -> resourceManager.getString(R.string.friday) to 4
            "saturday" -> resourceManager.getString(R.string.saturday) to 5
            "sunday" -> resourceManager.getString(R.string.sunday) to 6
            else -> resourceManager.getString(R.string.unknown_class_day) to 7
        }
    }

    fun updateSquadNumber(number: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            queriesInteractor.updateSquadNumber(id, number, object : ResponseCallback<String> {

                override fun success(data: String) {
                    draftUserInfo.currentUserInfo?.squadMember?.squad?.squadNumber = data
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun updateSquadClassDay(classDay: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            queriesInteractor.updateSquadClassDay(id, classDay, object : ResponseCallback<String> {

                override fun success(data: String) {
                    draftUserInfo.currentUserInfo?.squadMember?.squad?.classDay = data
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun updateSquadAnnouncement(announcement: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            queriesInteractor.updateSquadAnnouncement(id, announcement, object : ResponseCallback<String> {

                override fun success(data: String) {
                    draftUserInfo.currentUserInfo?.squadMember?.squad?.advertisment = data
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

    fun updateLinkInvitation(linkInvitation: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            queriesInteractor.updateLinkInvitation(id, linkInvitation, object : ResponseCallback<Boolean> {

                override fun success(data: Boolean) {
                    draftUserInfo.currentUserInfo?.squadMember?.squad?.linkInvitationsEnabled = data
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun acceptRequest(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.acceptRequest(id, object : ResponseCallback<ApproveSquadRequestMutation.ApproveSquadRequest> {

                override fun success(data: ApproveSquadRequestMutation.ApproveSquadRequest) {
                    val member = data.toMember()
                    draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.add(member)

                    val request = draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.find {
                        id == it.id
                    }
                    if (request != null) {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.remove(request)
                    }
                    viewState.updateRequest(id)
                }

                override fun error(error: String) {
                    viewState.showErrorMessage(error)
                }
            })
        }
    }

    fun rejectRequest(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queriesInteractor.deleteSquadRequest(id, object : ResponseCallback<DeleteSquadRequestMutation.Data> {

                override fun success(data: DeleteSquadRequestMutation.Data) {
                    val requestId = data.deleteSquadRequest?.id
                    val request = draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.find {
                        requestId == it.id
                    }
                    if (request != null) {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.remove(request)
                    }
                    viewState.updateRequest(requestId ?: "")
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