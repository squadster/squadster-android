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
import com.android.squadster.model.data.server.model.ResultApiCall
import com.android.squadster.model.system.resource.ResourceManager
import com.squadster.server.ApproveSquadRequestMutation
import com.squadster.server.DeleteSquadRequestMutation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            val result = queriesInteractor.updateSquadNumber(id, number)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.squadNumber = result.data
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun updateSquadClassDay(classDay: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            val result = queriesInteractor.updateSquadClassDay(id, classDay)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.classDay = result.data
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun updateSquadAnnouncement(announcement: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            val result = queriesInteractor.updateSquadAnnouncement(id, announcement)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.advertisment = result.data
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun deleteSquad() {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            val result = queriesInteractor.deleteSquad(id)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad = null
                        goToSquads()
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun updateLinkInvitation(linkInvitation: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = draftUserInfo.currentUserInfo?.squadMember?.squad?.id ?: ""
            val result = queriesInteractor.updateLinkInvitation(id, linkInvitation)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.linkInvitationsEnabled = result.data
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun acceptRequest(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.acceptRequest(id)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.add(result.data)

                        val request = draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.find {
                            id == it.id
                        }
                        if (request != null) {
                            draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.remove(request)
                        }
                        viewState.updateRequest(id)
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun rejectRequest(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.deleteSquadRequest(id)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        val requestId = result.data.deleteSquadRequest?.id
                        val request = draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.find {
                            requestId == it.id
                        }
                        if (request != null) {
                            draftUserInfo.currentUserInfo?.squadMember?.squad?.requests?.remove(request)
                        }
                        viewState.updateRequest(requestId ?: "")
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    private fun goToSquads() {
        flowRouter.replaceScreen(Screens.SquadsScreen)
    }
}