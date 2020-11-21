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

    override fun showUserInfo(nameAndSurname: String, birthday: String, faculty: String) {
        tv_name.text = nameAndSurname
        tv_birthday.text = birthday
        tv_faculty.text = faculty
    }

    private fun setupViews() {
        profilePresenter.loadUserAvatar(context, iv_avatar)

        btn_log_out.setOnClickListener {
            profilePresenter.logOut(context)
        }

        btn_about_us.setOnClickListener {

        }

        toolbar_profile.setNavigationOnClickListener {
            profilePresenter.onBackPressed()
        }
    }
}