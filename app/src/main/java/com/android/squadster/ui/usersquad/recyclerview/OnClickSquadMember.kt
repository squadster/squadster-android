package com.android.squadster.ui.usersquad.recyclerview

import com.android.squadster.model.data.server.model.Member
import com.android.squadster.model.data.server.model.UserMember

interface OnClickSquadMember {

    fun deleteMember(id: String, role: String)

    fun updateMemberRole(id: String, oldRole: String, quequeNumber: Int?)

    fun openMemberProfile(member: UserMember?)

    fun membersPositionsChanged(listOfMembers: ArrayList<Member>)
}