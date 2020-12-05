package com.android.squadster.ui.usersquad.recyclerview.studentstuff

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Member
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter.Companion.COMMANDER
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter.Companion.DEPUTY_COMMANDER
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter.Companion.JOURNALIST
import com.android.squadster.ui.usersquad.recyclerview.OnClickSquadMember
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_soldier.view.*

class StudentViewHolder(
    view: View,
    private val handler: OnClickSquadMember,
    private val currentUserId: String,
    private val isCurrentUserCommander: Boolean
) : RecyclerView.ViewHolder(view) {

    private lateinit var member: Member

    fun bindItem(member: Member) {
        this.member = member

        setupViews()
    }

    private fun setupViews() {
        loadMemberAvatar()

        itemView.tv_member_username.text = itemView.context.getString(
            R.string.name_and_lastname,
            member.user?.firstName,
            member.user?.lastName
        )
        itemView.tv_role.text = itemView.context.getString(R.string.student)

        itemView.iv_member_avatar.setOnClickListener {
            if (currentUserId != member.user?.id) {
                handler.openMemberProfile(member.user)
            }
        }

        if (isCurrentUserCommander && currentUserId != member.user?.id) {
            itemView.iv_delete.visibility = View.VISIBLE
            itemView.tv_role.setOnClickListener {
                handler.updateMemberRole(member.id, member.role.toString(), member.queueNumber)
            }
            itemView.iv_delete.setOnClickListener {
                member.id.let {
                    handler.deleteMember(member.id, member.role.toString())
                }
            }
        } else {
            itemView.iv_delete.visibility = View.GONE
        }
    }

    private fun loadMemberAvatar() {
        Glide.with(itemView.context)
            .load(member.user?.imageUrl)
            .circleCrop()
            .error(R.drawable.ic_soldier)
            .into(itemView.iv_member_avatar)
    }
}
