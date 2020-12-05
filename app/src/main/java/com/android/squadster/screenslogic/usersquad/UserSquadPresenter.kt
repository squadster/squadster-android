package com.android.squadster.screenslogic.usersquad

import com.android.squadster.R
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.interactor.QueriesInteractor
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.Member
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.data.server.model.ResultApiCall
import com.android.squadster.model.system.resource.ResourceManager
import com.apollographql.apollo.api.toInput
import com.squadster.server.*
import com.squadster.server.type.SquadMembersBatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
            if (member.user?.id == draftUserInfo.currentUserInfo?.id) {
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

    fun getCommandStuff(): ArrayList<Member> {
        val listOfMembers =
            draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.filter { member ->
                member.role != STUDENT
            }

        return ArrayList(listOfMembers ?: ArrayList<Member>())
    }

    fun getStudentStuff(): ArrayList<Member> {
        val listOfMembers =
            draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.filter { member ->
                member.role == STUDENT
            }?.sortedBy {
                it.queueNumber
            }

        return ArrayList(listOfMembers ?: ArrayList<Member>())
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

    fun deleteMember(id: String, role: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.deleteMember(id)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        val member = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.find {
                                it.id == result.data
                        }
                        if (member != null) {
                            draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.remove(member)
                            viewState.deleteSquadMember(result.data, role)
                        }
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun updateMemberRole(id: String, oldRole: String, newRole: String, queueNumber: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.updateMemberRole(id, newRole, queueNumber)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        val member = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.find {
                                it.id == result.data.id
                        }
                        if (member != null) {
                            val index = draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.indexOf(member)
                            member.queueNumber = result.data.queueNumber
                            member.role = result.data.role
                            if (index != null) {
                                draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.set(index, member)
                                viewState.updateSquadMemberRole(
                                    result.data.id,
                                    oldRole,
                                    result.data.role.toString(),
                                    result.data.queueNumber ?: 0
                                )
                            }
                        }
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
                        goToSquadsWithoutExit()
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    fun updateSquadQueue(students: ArrayList<Member>) {
        val batches = ArrayList<SquadMembersBatch>()
        students.forEachIndexed { index, member ->
            val batch = SquadMembersBatch(member.id, (index + 1).toInput())
            batches.add(batch)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val result = queriesInteractor.updateSquadQueue(batches)

            withContext(Dispatchers.Main) {
                when (result) {
                    is ResultApiCall.Success -> {
                        students.forEachIndexed { index, member ->
                            draftUserInfo.currentUserInfo?.squadMember?.squad?.members?.find {
                                it.id == member.id
                            }?.queueNumber = index + 1
                        }
                    }
                    is ResultApiCall.Error -> {
                        viewState.showErrorMessage(result.message)
                    }
                }
            }
        }
    }

    private fun goToSquadsWithoutExit() {
        flowRouter.replaceScreen(Screens.SquadsScreen)
    }

    companion object {
        const val COMMANDER = "commander"
        const val DEPUTY_COMMANDER = "deputy_commander"
        const val JOURNALIST = "journalist"
        const val STUDENT = "student"
    }
}