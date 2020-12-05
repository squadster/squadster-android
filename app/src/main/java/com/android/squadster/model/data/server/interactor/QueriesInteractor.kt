package com.android.squadster.model.data.server.interactor

import com.android.squadster.R
import com.android.squadster.di.provider.ApolloProvider
import com.android.squadster.extension.toUserInfo
import com.android.squadster.extension.toUserSquad
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.data.server.model.UserInfo
import com.android.squadster.model.data.server.model.UserSquad
import com.android.squadster.model.system.resource.ResourceManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.squadster.server.*
import com.squadster.server.type.SquadMembersBatch
import javax.inject.Inject

class QueriesInteractor @Inject constructor(
    private val apolloProvider: ApolloProvider,
    private val resourceManager: ResourceManager
) {

    /*suspend fun getCurrentUserInfoNew(): Flow<UserInfo> {
        return flow {
            apolloProvider.apolloClient.query(GetCurrentUserQuery())
                ?.enqueue(object : ApolloCall.Callback<GetCurrentUserQuery.Data>() {
                    override fun onResponse(response: Response<GetCurrentUserQuery.Data>) {
                        response.data?.currentUser?.let {
                            GlobalScope.launch(Dispatchers.Main) {
                                emit(it.toUserInfo())
                            }
                        }
                    }

                    override fun onFailure(e: ApolloException) {

                    }
                })
        }
    }*/

    fun getCurrentUserInfo(callback: ResponseCallback<UserInfo>) {
        apolloProvider.apolloClient.query(GetCurrentUserQuery())
            ?.enqueue(object : ApolloCall.Callback<GetCurrentUserQuery.Data>() {
                override fun onResponse(response: Response<GetCurrentUserQuery.Data>) {
                    response.data?.currentUser?.let {
                        callback.success(it.toUserInfo())
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun getSquads(callback: ResponseCallback<GetSquadsQuery.Data>) {
        apolloProvider.apolloClient.query(GetSquadsQuery())
            ?.enqueue(object : ApolloCall.Callback<GetSquadsQuery.Data>() {
                override fun onResponse(response: Response<GetSquadsQuery.Data>) {
                    response.data?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun createSquadRequest(
        id: String,
        callback: ResponseCallback<CreateSquadRequestMutation.Data>
    ) {
        apolloProvider.apolloClient.mutate(CreateSquadRequestMutation(squadId = id))
            ?.enqueue(object : ApolloCall.Callback<CreateSquadRequestMutation.Data>() {
                override fun onResponse(response: Response<CreateSquadRequestMutation.Data>) {
                    response.data?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun deleteSquadRequest(
        id: String,
        callback: ResponseCallback<DeleteSquadRequestMutation.Data>
    ) {
        apolloProvider.apolloClient.mutate(DeleteSquadRequestMutation(id = id))
            ?.enqueue(object : ApolloCall.Callback<DeleteSquadRequestMutation.Data>() {
                override fun onResponse(response: Response<DeleteSquadRequestMutation.Data>) {
                    response.data?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun deleteMember(
        id: String,
        callback: ResponseCallback<DeleteSquadMemberMutation.Data>
    ) {
        apolloProvider.apolloClient.mutate(DeleteSquadMemberMutation(id = id))
            ?.enqueue(object : ApolloCall.Callback<DeleteSquadMemberMutation.Data>() {
                override fun onResponse(response: Response<DeleteSquadMemberMutation.Data>) {
                    response.data?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateMemberRole(
        id: String,
        role: String,
        quequeNumber: Int,
        callback: ResponseCallback<UpdateSquadMemberMutation.Data>
    ) {
        apolloProvider.apolloClient
            .mutate(UpdateSquadMemberMutation(id = id, role = role, quequeNumber = quequeNumber))
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadMemberMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadMemberMutation.Data>) {
                    response.data?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun createSquad(
        squadNumber: String,
        classDay: Int,
        callback: ResponseCallback<UserSquad>
    ) {
        apolloProvider.apolloClient.mutate(
            CreateSquadMutation(
                squad_number = squadNumber,
                class_day = classDay
            )
        )
            ?.enqueue(object : ApolloCall.Callback<CreateSquadMutation.Data>() {
                override fun onResponse(response: Response<CreateSquadMutation.Data>) {
                    response.data?.createSquad?.let {
                        callback.success(it.toUserSquad())
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateSquadNumber(
        id: String,
        number: String,
        callback: ResponseCallback<String>
    ) {
        apolloProvider.apolloClient.mutate(UpdateSquadNumberMutation(id = id, number = number))
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadNumberMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadNumberMutation.Data>) {
                    response.data?.updateSquad?.squadNumber?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateSquadClassDay(
        id: String,
        classDay: Int,
        callback: ResponseCallback<String>
    ) {
        apolloProvider.apolloClient.mutate(
            UpdateSquadClassDayMutation(
                id = id,
                classDay = classDay
            )
        )
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadClassDayMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadClassDayMutation.Data>) {
                    response.data?.updateSquad?.classDay?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateSquadAnnouncement(
        id: String,
        announcement: String,
        callback: ResponseCallback<String>
    ) {
        apolloProvider.apolloClient.mutate(
            UpdateSquadAnnouncementMutation(
                id = id,
                advertisment = announcement
            )
        )
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadAnnouncementMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadAnnouncementMutation.Data>) {
                    response.data?.updateSquad?.advertisment?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun deleteSquad(
        id: String,
        callback: ResponseCallback<Boolean>
    ) {
        apolloProvider.apolloClient.mutate(DeleteSquadMutation(id = id))
            ?.enqueue(object : ApolloCall.Callback<DeleteSquadMutation.Data>() {
                override fun onResponse(response: Response<DeleteSquadMutation.Data>) {
                    response.data?.deleteSquad?.id?.let {
                        callback.success(true)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateLinkInvitation(
        id: String,
        linkInvitationEnabled: Boolean,
        callback: ResponseCallback<Boolean>
    ) {
        apolloProvider.apolloClient.mutate(
            UpdateSquadLinkOptionMutation(
                id = id,
                linkOption = linkInvitationEnabled
            )
        )
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadLinkOptionMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadLinkOptionMutation.Data>) {
                    response.data?.updateSquad?.linkInvitationsEnabled?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun acceptRequest(
        id: String,
        callback: ResponseCallback<ApproveSquadRequestMutation.ApproveSquadRequest>
    ) {
        apolloProvider.apolloClient.mutate(ApproveSquadRequestMutation(id = id))
            ?.enqueue(object : ApolloCall.Callback<ApproveSquadRequestMutation.Data>() {
                override fun onResponse(response: Response<ApproveSquadRequestMutation.Data>) {
                    response.data?.approveSquadRequest?.let {
                        callback.success(it)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }

    fun updateSquadQueue(
        list: List<SquadMembersBatch>,
        callback: ResponseCallback<Boolean>
    ) {
        apolloProvider.apolloClient.mutate(UpdateSquadMembersMutation(members = list))
            ?.enqueue(object : ApolloCall.Callback<UpdateSquadMembersMutation.Data>() {
                override fun onResponse(response: Response<UpdateSquadMembersMutation.Data>) {
                    response.data?.let {
                        callback.success(true)
                    } ?: run {
                        callback.error(resourceManager.getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.error(
                        e.message ?: resourceManager.getString(R.string.something_went_wrong)
                    )
                }
            })
    }
}