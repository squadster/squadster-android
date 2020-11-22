package com.android.squadster.screenslogic.profile

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.widget.ImageView
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.core.Screens
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.android.squadster.model.system.resource.ResourceManager
import com.bumptech.glide.Glide
import moxy.InjectViewState
import java.io.File
import javax.inject.Inject


@InjectViewState
class ProfilePresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager,
    private val draftUserInfo: DraftUserInfo
) : BasePresenter<ProfileView>() {

    fun onBackPressed() {
        flowRouter.exit()
    }

    fun logOut(context: Context?) {
        val result = (context!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .clearApplicationUserData()
        if (result) flowRouter.finishFlow()
    }

    fun loadUserAvatar(context: Context?, view: ImageView) {
        if (context != null) {
            Glide.with(context)
                .load(draftUserInfo.userInfo?.imageUrl)
                .circleCrop()
                .into(view)

            viewState.showUserInfo(
                draftUserInfo.userInfo?.firstName + " " + draftUserInfo.userInfo?.lastName,
                draftUserInfo.userInfo?.birthDate.toString(),
                draftUserInfo.userInfo?.university + " " + draftUserInfo.userInfo?.faculty
            )
        }
    }
}