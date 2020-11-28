package com.android.squadster.ui.usersquad

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.model.data.server.model.UserMember
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter
import com.android.squadster.screenslogic.usersquad.UserSquadView
import com.android.squadster.ui.usersquad.dialog.UpdateMemberRoleDialog
import com.android.squadster.ui.usersquad.recyclerview.MembersAdapter
import com.android.squadster.ui.usersquad.recyclerview.OnClickSquadMember
import kotlinx.android.synthetic.main.fragment_user_squad.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope


class UserSquadFragment : BaseFragment(), UserSquadView, OnClickSquadMember {

    override val layoutRes = R.layout.fragment_user_squad

    private lateinit var membersAdapter: MembersAdapter

    private var isCurrentUserCommander = false

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var userSquadPresenter: UserSquadPresenter

    @ProvidePresenter
    fun providePresenter(): UserSquadPresenter =
        scope.getInstance(UserSquadPresenter::class.java)

    override fun onBackPressed() {
        userSquadPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_squad, menu)

        isCurrentUserCommander = userSquadPresenter.isCurrentUserCommander()

        if (!isCurrentUserCommander) {
            val item = menu.findItem(R.id.mi_settings)
            item.isVisible = false
        }
    }

    override fun deleteMember(id: String) {
        userSquadPresenter.deleteMember(id)
    }

    override fun updateMemberRole(id: String, role: String, quequeNumber: Int?) {
        val updateMemberRoleDialog = UpdateMemberRoleDialog(
            requireContext(),
            id,
            role,
            quequeNumber ?: 0,
            ::updateMember)
        updateMemberRoleDialog.show()
    }

    override fun openMemberProfile(member: UserMember?) {
        userSquadPresenter.draftUserInfo.anotherUser = member
        userSquadPresenter.goToProfile()
    }

    override fun showErrorMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun deleteSquadMember(id: String) {
        activity?.runOnUiThread {
            membersAdapter.deleteMember(id)
        }
    }

    override fun updateSquadMemberRole(id: String, role: String, quequeNumber: Int) {
        activity?.runOnUiThread {
            membersAdapter.updateMember(id, role, quequeNumber)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_settings -> {
                userSquadPresenter.goToSquadSettings()
                true
            }
            R.id.mi_profile -> {
                userSquadPresenter.goToProfile()
                true
            }
            R.id.mi_squads -> {
                userSquadPresenter.goToSquads()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        (activity as MvpAppCompatActivity?)?.setSupportActionBar(toolbar_user_squad)
        setHasOptionsMenu(true)

        val squadNumber =
            userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.squadNumber ?: getString(
                R.string.unknown_squad_number
            )
        toolbar_user_squad.title = getString(R.string.user_squad, squadNumber)

        val classDay = userSquadPresenter.getClassDay()
        txt_class_day.text = getString(R.string.class_day, classDay)

        val announcement =
            userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.advertisment
                ?: getString(
                    R.string.none_announcement
                )
        txt_announcement.text = getString(R.string.announcement, announcement)


        membersAdapter = MembersAdapter(
            this,
            userSquadPresenter.draftUserInfo.currentUserInfo?.id ?: "",
            userSquadPresenter.isCurrentUserCommander()
        )
        val llManager = LinearLayoutManager(context)
        rv_soldiers.layoutManager = llManager
        rv_soldiers.adapter = membersAdapter
        membersAdapter.setData(userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.members)
    }

    private fun updateMember(id: String, role: String, quequeNumber: Int){
        userSquadPresenter.updateMemberRole(id, role, quequeNumber)
    }
}