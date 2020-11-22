package com.android.squadster.ui.squads.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.android.squadster.R
import kotlinx.android.synthetic.main.dialog_create_squad.*

class CreateSquadDialog(
    context: Context,
    private val handler: (String, Int) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_create_squad)

        setupViews()
    }

    private fun setupViews() {
        et_squad_number.doOnTextChanged { text, start, before, count ->
            btn_create_squad.isEnabled = !text.isNullOrEmpty()
        }
        btn_cross.setOnClickListener {
            dismiss()
        }

        btn_create_squad.setOnClickListener {
            handler(et_squad_number.text.toString(), sp_class_day.selectedItemPosition + 1)
            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()

        window?.let { w ->
            w.setBackgroundDrawable(null)
            w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}