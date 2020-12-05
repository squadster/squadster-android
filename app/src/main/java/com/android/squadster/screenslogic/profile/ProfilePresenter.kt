package com.android.squadster.screenslogic.profile

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.widget.ImageView
import com.android.squadster.R
import com.android.squadster.core.BasePresenter
import com.android.squadster.core.FlowRouter
import com.android.squadster.model.data.server.model.DraftUserInfo
import com.bumptech.glide.Glide
import moxy.InjectViewState
import javax.inject.Inject


@InjectViewState
class ProfilePresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    val draftUserInfo: DraftUserInfo
) : BasePresenter<ProfileView>() {

    fun onBackPressed() {
        flowRouter.exit()
    }

    fun logOut(context: Context?) {
        val result = (context!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .clearApplicationUserData()
        if (result) flowRouter.finishFlow()
    }

    fun loadUserAvatar(context: Context?, view: ImageView, imageUrl: String?) {
        if (context != null) {
            Glide.with(context)
                .load(imageUrl)
                .error(R.drawable.ic_soldier)
                .circleCrop()
                .into(view)
        }
    }
}