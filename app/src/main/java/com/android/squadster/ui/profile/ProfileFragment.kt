package com.android.squadster.ui.profile

import android.os.Bundle
import android.view.View
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.screenslogic.profile.ProfilePresenter
import com.android.squadster.screenslogic.profile.ProfileView
import com.android.squadster.screenslogic.squads.SquadsPresenter
import com.android.squadster.screenslogic.squads.SquadsView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_squad_settings.*
import kotlinx.android.synthetic.main.fragment_squads.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class ProfileFragment : BaseFragment(), ProfileView {

    override val layoutRes = R.layout.fragment_profile

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter =
        scope.getInstance(ProfilePresenter::class.java)

    override fun onBackPressed() {
        profilePresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        profilePresenter.draftUserInfo.anotherUser?.let { user ->
            profilePresenter.loadUserAvatar(context, iv_avatar, user.imageUrl)

            tv_name.text = getString(R.string.name_and_lastname, user.firstName, user.lastName)
            tv_birthday.text = profilePresenter.draftUserInfo.currentUserInfo?.birthDate.toString()
            tv_faculty.text = getString(R.string.name_and_lastname, user.university, user.faculty)

            btn_log_out.visibility = View.GONE
            btn_about_us.visibility = View.GONE
        } ?: run {
            profilePresenter.loadUserAvatar(
                context,
                iv_avatar,
                profilePresenter.draftUserInfo.currentUserInfo?.imageUrl
            )

            tv_name.text = getString(
                R.string.name_and_lastname,
                profilePresenter.draftUserInfo.currentUserInfo?.firstName,
                profilePresenter.draftUserInfo.currentUserInfo?.lastName
            )

            tv_birthday.text = profilePresenter.draftUserInfo.currentUserInfo?.birthDate.toString()

            tv_faculty.text = getString(
                R.string.name_and_lastname,
                profilePresenter.draftUserInfo.currentUserInfo?.university,
                profilePresenter.draftUserInfo.currentUserInfo?.faculty
            )

            btn_log_out.setOnClickListener {
                profilePresenter.logOut(context)
            }

            btn_about_us.setOnClickListener {

            }
        }

        profilePresenter.draftUserInfo.anotherUser = null

        toolbar_profile.setNavigationOnClickListener {
            profilePresenter.onBackPressed()
        }
    }
}