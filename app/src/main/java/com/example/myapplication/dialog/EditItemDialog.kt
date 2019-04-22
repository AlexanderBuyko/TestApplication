package com.example.myapplication.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.api.models.Jog.Jog

@SuppressLint("ValidFragment")
class EditItemDialog(currentJog: Jog, editItemInterface: EditItemInterface) : AppCompatDialogFragment() {

    private var editDistance: TextInputLayout? = null
    private var editTime: TextInputLayout? = null

    private var jog: Jog = currentJog
    private var editItemInterface: EditItemInterface = editItemInterface

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity?.let { AlertDialog.Builder(it) }!!
        val inflater: LayoutInflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)


        builder.setView(view)
            .setTitle("Edit this Jog")
            .setPositiveButton("Save") { _, _ ->
                val distanceStr = editDistance?.editText?.text.toString()
                val timeStr = editTime?.editText?.text.toString()
                editItemInterface.editJog?.invoke(distanceStr.toFloat(), timeStr.toInt(), jog)
            }
            .setNegativeButton("cancel") { _, _ ->

            }
            .setNeutralButton("Delete"){_, _ ->
                editItemInterface.deleteJog?.invoke(jog)
            }
        editDistance = view.findViewById(R.id.text_input_distance)
        editTime = view.findViewById(R.id.text_input_time)
        return builder.create()
    }
}
interface EditItemInterface {
    var editJog: ((Float, Int, Jog) -> Unit)?
    var deleteJog: ((Jog) -> Unit)?

}