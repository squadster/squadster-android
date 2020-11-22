package com.android.squadster.ui.usersquad

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter
import com.android.squadster.screenslogic.usersquad.UserSquadView
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

    /*override fun setSquads(squads: List<GetSquadsQuery.Squad>) {
        srl_update_squads_list.isRefreshing = false
        activity?.runOnUiThread {
            if (squads.isEmpty()) {
                showEmptyListOfSquads()
            } else {
                cl_addition_info.visibility = View.VISIBLE
                rv_squads.visibility = View.VISIBLE
                squadsAdapter.setData(ArrayList(squads))
            }
        }
    }

    override fun updateSquadInvitation(
        squadId: String,
        requestId: String,
        requestStatus: RequestStatus
    ) {
        activity?.runOnUiThread {
            squadsAdapter.updateSquad(squadId, requestId, requestStatus)
        }
    }

    override fun showErrorMessage(message: String) {
        srl_update_squads_list.isRefreshing = false
        activity?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showEmptyListOfSquads() {
        activity?.runOnUiThread {
            cl_addition_info.visibility = View.GONE
            rv_squads.visibility = View.GONE
            tv_no_squads_message.visibility = View.VISIBLE
        }
    }

    override fun sendRequest(squadId: String) {
        squadsPresenter.sendRequest(squadId)
    }

    override fun cancelRequest(requestId: String?) {
        squadsPresenter.cancelRequest(requestId)
    }*/

    override fun deleteMember(id: Int) {
        userSquadPresenter.deleteMember(id)
    }

    override fun updateMemberRole(id: Int, role: String, quequeNumber: Int) {
        userSquadPresenter.updateMemberRole(id, role, quequeNumber)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_settings -> {

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
            userSquadPresenter.draftUserInfo.userInfo?.squadMember?.squad?.squadNumber ?: getString(
                R.string.unknown_squad_number
            )
        toolbar_user_squad.title = getString(R.string.user_squad, squadNumber)

        val classDay = userSquadPresenter.getClassDay()
        txt_class_day.text = getString(R.string.class_day, classDay)

        val announcement =
            userSquadPresenter.draftUserInfo.userInfo?.squadMember?.squad?.advertisment
                ?: getString(
                    R.string.none_announcement
                )
        txt_announcement.text = getString(R.string.announcement, announcement)


        membersAdapter = MembersAdapter(
            this,
            userSquadPresenter.draftUserInfo.userInfo?.id ?: "",
            isCurrentUserCommander
        )
        val llManager = LinearLayoutManager(context)
        rv_soldiers.layoutManager = llManager
        rv_soldiers.adapter = membersAdapter
        membersAdapter.setData(userSquadPresenter.draftUserInfo.userInfo?.squadMember?.squad?.members)
    }

    /*private fun createSquad(squadNumber: String, classDay:Int){
        squadsPresenter.createSquad(squadNumber, classDay)
    }*/
}