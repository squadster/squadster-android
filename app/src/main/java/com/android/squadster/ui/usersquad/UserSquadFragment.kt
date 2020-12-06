package com.android.squadster.ui.usersquad

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.model.data.server.model.Member
import com.android.squadster.model.data.server.model.UserMember
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter
import com.android.squadster.screenslogic.usersquad.UserSquadView
import com.android.squadster.ui.usersquad.dialog.UpdateMemberRoleDialog
import com.android.squadster.ui.usersquad.recyclerview.OnClickSquadMember
import com.android.squadster.ui.usersquad.recyclerview.commandstuff.CommandersAdapter
import com.android.squadster.ui.usersquad.recyclerview.studentstuff.DragAndDropCallback
import com.android.squadster.ui.usersquad.recyclerview.studentstuff.StudentsAdapter
import kotlinx.android.synthetic.main.fragment_user_squad.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope


class UserSquadFragment : BaseFragment(), UserSquadView, OnClickSquadMember {

    override val layoutRes = R.layout.fragment_user_squad

    private lateinit var studentsAdapter: StudentsAdapter
    private lateinit var commandersAdapter: CommandersAdapter

    override fun installScopeModules(scope: Scope) {}

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
        if (userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.linkInvitationsEnabled == false) {
            menu.findItem(R.id.mi_squad_link).isVisible = false
        }
    }

    override fun deleteMember(id: String, role: String) {
        userSquadPresenter.deleteMember(id, role)
    }

    override fun updateMemberRole(id: String, oldRole: String, quequeNumber: Int?) {
        val updateMemberRoleDialog = UpdateMemberRoleDialog(
            requireContext(),
            id,
            oldRole,
            quequeNumber ?: 0,
            ::updateMember
        )
        updateMemberRoleDialog.show()
    }

    override fun openMemberProfile(member: UserMember?) {
        userSquadPresenter.draftUserInfo.anotherUser = member
        userSquadPresenter.goToProfile()
    }

    override fun membersPositionsChanged(listOfMembers: ArrayList<Member>) {
        userSquadPresenter.updateSquadQueue(listOfMembers)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun deleteSquadMember(id: String, role: String) {
        when (role) {
            UserSquadPresenter.STUDENT -> {
                studentsAdapter.deleteMember(id)
                if (studentsAdapter.isListOfMembersIsEmpty()) {
                    rv_soldiers.visibility = View.INVISIBLE
                    tv_empty_students_message.visibility = View.VISIBLE
                }
            }
            else -> {
                commandersAdapter.deleteMember(id)
                if (commandersAdapter.isListOfMembersIsEmpty()) {
                    rv_commanders.visibility = View.INVISIBLE
                    tv_empty_commanders_message.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun updateSquadMemberRole(
        id: String,
        oldRole: String,
        newRole: String,
        quequeNumber: Int
    ) {
        when {
            oldRole == UserSquadPresenter.STUDENT -> {
                val member = studentsAdapter.findMember(id) ?: return
                studentsAdapter.deleteMember(id)
                member.role = newRole
                member.queueNumber = quequeNumber
                commandersAdapter.addMember(member)

                if (studentsAdapter.isListOfMembersIsEmpty()) {
                    rv_soldiers.visibility = View.INVISIBLE
                    tv_empty_students_message.visibility = View.VISIBLE
                }

                if (commandersAdapter.itemCount == 1) {
                    rv_commanders.visibility = View.VISIBLE
                    tv_empty_commanders_message.visibility = View.INVISIBLE
                }
            }
            newRole == UserSquadPresenter.STUDENT -> {
                val member = commandersAdapter.findMember(id) ?: return
                commandersAdapter.deleteMember(id)
                member.role = newRole
                member.queueNumber = quequeNumber
                studentsAdapter.addMember(member)

                if (commandersAdapter.isListOfMembersIsEmpty()) {
                    rv_commanders.visibility = View.INVISIBLE
                    tv_empty_commanders_message.visibility = View.VISIBLE
                }

                if (studentsAdapter.itemCount == 1) {
                    rv_soldiers.visibility = View.VISIBLE
                    tv_empty_students_message.visibility = View.INVISIBLE
                }
            }
            else -> {
                commandersAdapter.updateMember(id, newRole, quequeNumber)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_settings -> {
                val isCurrentUserCommander = userSquadPresenter.isCurrentUserCommander()
                if (isCurrentUserCommander) {
                    userSquadPresenter.goToSquadSettings()
                } else {
                    AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.need_confirmation)
                        .setMessage(R.string.delete_squad_user_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes) { _, _ -> userSquadPresenter.deleteSquad() }
                        .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
                        .show()
                }
                true
            }
            R.id.mi_squad_link -> {
                val hash = userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.hashId
                val link = getString(R.string.squad_link, hash)
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link", link)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, R.string.link_copied, Toast.LENGTH_LONG).show()
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
            userSquadPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.squadNumber
                ?: getString(
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

        commandersAdapter = CommandersAdapter(
            this,
            userSquadPresenter.draftUserInfo.currentUserInfo?.id ?: "",
            userSquadPresenter.isCurrentUserCommander()
        )

        studentsAdapter = StudentsAdapter(
            this,
            userSquadPresenter.draftUserInfo.currentUserInfo?.id ?: "",
            userSquadPresenter.isCurrentUserCommander()
        )

        /*val listOfCommanders = arrayListOf(
            Member(
                id = "1",
                role = "commander",
                queueNumber = 2,
                UserMember(
                    id = "1",
                    firstName = "Name1",
                    lastName = "Surname1",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "2",
                role = "journalist",
                queueNumber = 1,
                UserMember(
                    id = "1",
                    firstName = "Name2",
                    lastName = "Surname2",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = null,
                    smallImageUrl = null,
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "3",
                role = "journalist",
                queueNumber = 3,
                UserMember(
                    id = "1",
                    firstName = "Name3",
                    lastName = "Surname3",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "4",
                role = "deputy_commander",
                queueNumber = 4,
                UserMember(
                    id = "4",
                    firstName = "Name4",
                    lastName = "Surname4",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "5",
                role = "commander",
                queueNumber = 5,
                UserMember(
                    id = "5",
                    firstName = "Name5",
                    lastName = "Surname5",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            )
        )

        val listOfUsers = arrayListOf(
            Member(
                id = "6",
                role = "student",
                queueNumber = 2,
                UserMember(
                    id = "1",
                    firstName = "Name1",
                    lastName = "Surname1",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "7",
                role = "student",
                queueNumber = 1,
                UserMember(
                    id = "1",
                    firstName = "Name2",
                    lastName = "Surname2",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = null,
                    smallImageUrl = null,
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "8",
                role = "student",
                queueNumber = 3,
                UserMember(
                    id = "1",
                    firstName = "Name3",
                    lastName = "Surname3",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "9",
                role = "student",
                queueNumber = 4,
                UserMember(
                    id = "4",
                    firstName = "Name4",
                    lastName = "Surname4",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            ),
            Member(
                id = "10",
                role = "student",
                queueNumber = 5,
                UserMember(
                    id = "5",
                    firstName = "Name5",
                    lastName = "Surname5",
                    mobilePhone = "1123",
                    faculty = "JOPA",
                    university = "JOPA",
                    imageUrl = "",
                    smallImageUrl = "",
                    vkUrl = "",
                    birthDate = ""
                )
            )
        )*/

        val llManagerCommanders = LinearLayoutManager(context)
        rv_commanders.layoutManager = llManagerCommanders
        rv_commanders.adapter = commandersAdapter
        commandersAdapter.setData(userSquadPresenter.getCommandStuff())
        if (commandersAdapter.isListOfMembersIsEmpty()) {
            rv_commanders.visibility = View.INVISIBLE
            tv_empty_commanders_message.visibility = View.VISIBLE
        }

        val llManagerStudents = LinearLayoutManager(context)
        rv_soldiers.layoutManager = llManagerStudents
        rv_soldiers.adapter = studentsAdapter
        studentsAdapter.setData(userSquadPresenter.getStudentStuff())
        if (studentsAdapter.isListOfMembersIsEmpty()) {
            rv_soldiers.visibility = View.INVISIBLE
            tv_empty_students_message.visibility = View.VISIBLE
        }

        if (userSquadPresenter.isCurrentUserCommander()) {
            val callback = DragAndDropCallback(studentsAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(rv_soldiers)
        }
    }

    private fun updateMember(id: String, oldRole: String, newRole: String, quequeNumber: Int) {
        userSquadPresenter.updateMemberRole(id, oldRole, newRole, quequeNumber)
    }
}