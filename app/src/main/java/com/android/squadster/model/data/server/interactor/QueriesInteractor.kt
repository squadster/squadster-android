package com.android.squadster.model.data.server.interactor

import com.android.squadster.R
import com.android.squadster.di.provider.ApolloProvider
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.data.server.model.ResponseCallback
import com.android.squadster.model.system.resource.ResourceManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.squadster.server.CreateSquadRequestMutation
import com.squadster.server.DeleteSquadRequestMutation
import com.squadster.server.GetSquadsQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QueriesInteractor @Inject constructor(
    private val apolloProvider: ApolloProvider,
    private val draftUserInfo: DraftUserInfo,
    private val resourceManager: ResourceManager
) {

    fun getSquads(callback: ResponseCallback<GetSquadsQuery.Data>) {
        apolloProvider.getClient().query(GetSquadsQuery())
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
        apolloProvider.getClient().mutate(CreateSquadRequestMutation(squadId = id))
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
        apolloProvider.getClient().mutate(DeleteSquadRequestMutation(id = id))
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
}