package com.android.squadster.model.data.server.model

import com.squadster.server.GetSquadsQuery

data class Squad(
    val id: String,
    val squadNumber: String,
    val linkInvitationsEnabled: Boolean,
    val members: ArrayList<GetSquadsQuery.Member>,
    val requests: ArrayList<GetSquadsQuery.Request>,
)