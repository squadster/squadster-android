package com.android.squadster.ui.usersquad.recyclerview

interface OnClickSquadMember {

    fun deleteMember(id: Int)

    fun updateMemberRole(id: Int, role: String, quequeNumber: Int)
}