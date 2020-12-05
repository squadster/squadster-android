package com.android.squadster.model.data.server.interactor

import com.android.squadster.R
import com.android.squadster.di.provider.ApolloProvider
import com.android.squadster.extension.toMember
import com.android.squadster.extension.toSquad
import com.android.squadster.extension.toUserInfo
import com.android.squadster.extension.toUserSquad
import com.android.squadster.model.data.server.model.*
import com.android.squadster.model.system.resource.ResourceManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.squadster.server.*
import com.squadster.server.type.SquadMembersBatch
import javax.inject.Inject

class QueriesInteractor @Inject constructor(
    private val apolloProvider: ApolloProvider,
    private val resourceManager: ResourceManager
) {

    suspend fun getCurrentUserInfo(): ResultApiCall<UserInfo> {
        val response = try {
            apolloProvider.apolloClient.query(GetCurrentUserQuery()).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.currentUser?.let {
            return ResultApiCall.Success(it.toUserInfo())
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun getSquads(): ResultApiCall<List<Squad>> {
        val response = try {
            apolloProvider.apolloClient.query(GetSquadsQuery()).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.let { data ->
            data.squads?.let { squads ->
                return ResultApiCall.Success(
                    squads.filterNotNull().map {
                        it.toSquad()
                    }
                )
            } ?: run {
                return ResultApiCall.Success(ArrayList())
            }
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun createSquadRequest(id: String): ResultApiCall<CreateSquadRequestMutation.Data> {
        val response = try {
            apolloProvider.apolloClient.mutate(CreateSquadRequestMutation(squadId = id)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun deleteSquadRequest(id: String): ResultApiCall<DeleteSquadRequestMutation.Data> {
        val response = try {
            apolloProvider.apolloClient.mutate(DeleteSquadRequestMutation(id = id)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun deleteMember(id: String): ResultApiCall<String> {
        val response = try {
            apolloProvider.apolloClient.mutate(DeleteSquadMemberMutation(id = id)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.deleteSquadMember?.let {
            return ResultApiCall.Success(it.id)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateMemberRole(id: String, role: String, queueNumber: Int): ResultApiCall<UpdateSquadMemberMutation.UpdateSquadMember> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadMemberMutation(id = id, role = role, queueNumber = queueNumber)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.updateSquadMember?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun createSquad(squadNumber: String, classDay: Int): ResultApiCall<UserSquad> {
        val response = try {
            apolloProvider.apolloClient.mutate(CreateSquadMutation(squad_number = squadNumber, class_day = classDay)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.createSquad?.let {
            return ResultApiCall.Success(it.toUserSquad())
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateSquadNumber(id: String, number: String): ResultApiCall<String> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadNumberMutation(id = id, number = number)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.updateSquad?.squadNumber?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateSquadClassDay(id: String, classDay: Int): ResultApiCall<String> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadClassDayMutation(id = id, classDay = classDay)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.updateSquad?.classDay?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateSquadAnnouncement(id: String, announcement: String): ResultApiCall<String> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadAnnouncementMutation(id = id, advertisment = announcement)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.updateSquad?.advertisment?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun deleteSquad(id: String): ResultApiCall<Boolean> {
        val response = try {
            apolloProvider.apolloClient.mutate(DeleteSquadMutation(id = id)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.deleteSquad?.id?.let {
            return ResultApiCall.Success(true)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateLinkInvitation(id: String, linkInvitationEnabled: Boolean): ResultApiCall<Boolean> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadLinkOptionMutation(id = id, linkOption = linkInvitationEnabled)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.updateSquad?.linkInvitationsEnabled?.let {
            return ResultApiCall.Success(it)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun acceptRequest(id: String): ResultApiCall<Member> {
        val response = try {
            apolloProvider.apolloClient.mutate(ApproveSquadRequestMutation(id = id)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.approveSquadRequest?.let {
            return ResultApiCall.Success(it.toMember())
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }

    suspend fun updateSquadQueue(list: List<SquadMembersBatch>): ResultApiCall<Boolean> {
        val response = try {
            apolloProvider.apolloClient.mutate(UpdateSquadMembersMutation(members = list)).await()
        } catch (ex: Exception) {
            return ResultApiCall.Error(
                -1,
                ex.message ?: resourceManager.getString(R.string.something_went_wrong)
            )
        }

        response.data?.let {
            return ResultApiCall.Success(true)
        } ?: run {
            return ResultApiCall.Error(-1, resourceManager.getString(R.string.something_went_wrong))
        }
    }
}