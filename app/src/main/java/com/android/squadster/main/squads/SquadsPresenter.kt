package com.android.squadster.main.squads

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.model.system.resource.ResourceManager
import moxy.InjectViewState
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.model.data.server.apolloClient
import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.squadster.server.GetCurrentUserQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@InjectViewState
class SquadsPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    private val draftUserInfo: DraftUserInfo
) : BasePresenter<SquadsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        /*GlobalScope.launch(Dispatchers.IO) {
            draftUserInfo.userInfo?.user?.token?.let {
                val response = apolloClient(it).query(GetCurrentUserQuery()).await()

                Log.d("LaunchList", "Success ${response.data}")
            }
        }*/
    }

    fun loadUserAvatar(context: Context, view: ImageView) {
        Glide.with(context)
            .load(draftUserInfo.userInfo?.user?.imageUrl)
            .circleCrop()
            .into(view);
    }

    fun onBackPressed() {
        flowRouter.finishFlow()
    }
}