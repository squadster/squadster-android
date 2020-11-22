package com.android.squadster.ui.usersquad.recyclerview

interface OnClickSquadMember {

    fun deleteMember(id: String)

    fun updateMemberRole(id: String, role: String, quequeNumber: Int)
}