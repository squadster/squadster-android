package com.android.squadster.ui.usersquad.recyclerview

import com.android.squadster.model.data.server.model.UserMember

interface OnClickSquadMember {

    fun deleteMember(id: String)

    fun updateMemberRole(id: String, role: String, quequeNumber: Int?)

    fun openMemberProfile(member: UserMember?)
}