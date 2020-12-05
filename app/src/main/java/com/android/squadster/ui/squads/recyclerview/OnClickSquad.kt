package com.android.squadster.ui.squads.recyclerview

interface OnClickSquad {

    fun sendRequest(squadId: String)

    fun cancelRequest(requestId: String?)
}