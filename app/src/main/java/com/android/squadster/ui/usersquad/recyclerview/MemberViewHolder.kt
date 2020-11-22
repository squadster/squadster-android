package com.android.squadster.ui.usersquad.recyclerview

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Member
import com.android.squadster.model.data.server.model.Squad
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
    private var currentPosition: Int = 0

    fun bindItem(member: Member, position: Int) {
        this.member = member
        currentPosition = position

        setupViews()
    }

    private fun setupViews() {
        loadMemberAvatar()
        itemView.tv_member_username.text = "${member.user?.firstName}  ${member.user?.lastName}"
        itemView.tv_role.text = when (member.role) {
            COMMANDER -> itemView.context.getString(R.string.commander)
            SECOND_IN_COMMAND -> itemView.context.getString(R.string.second_in_command)
            JOURNALIST -> itemView.context.getString(R.string.journalist)
            else -> itemView.context.getString(R.string.soldier)
        }
        if (isCurrentUserCommander && currentUserId != member.user?.id) {
            itemView.iv_delete.visibility = View.VISIBLE
            itemView.tv_role.setOnClickListener {
                //handler.updateMemberRole()
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
        private const val COMMANDER = "commander"
        private const val SECOND_IN_COMMAND = "second_in_command"
        private const val JOURNALIST = "journalist"
        private const val SOLDIER = "soldier"
    }
}
