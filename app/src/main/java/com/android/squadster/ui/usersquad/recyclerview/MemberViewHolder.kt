package com.android.squadster.ui.usersquad.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Member
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_soldier.view.*
import kotlinx.android.synthetic.main.item_squad.view.*

class MemberViewHolder(
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
        itemView.tv_member_username.text = "${member.user?.firstName}  ${member.user?.lastName}"
        itemView.tv_role.text = when (member.role) {
            COMMANDER -> itemView.context.getString(R.string.commander)
            DEPUTY_COMMANDER -> itemView.context.getString(R.string.deputy_commander)
            JOURNALIST -> itemView.context.getString(R.string.journalist)
            else -> itemView.context.getString(R.string.student)
        }
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
                    handler.deleteMember(member.id)
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
            .into(itemView.iv_member_avatar)
    }

    companion object {
        const val COMMANDER = "commander"
        const val DEPUTY_COMMANDER = "deputy_commander"
        const val JOURNALIST = "journalist"
        const val STUDENT = "student"
    }
}
