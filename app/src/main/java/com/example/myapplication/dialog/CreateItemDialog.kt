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

@SuppressLint("ValidFragment")
class CreateItemDialog(createItemInterface: CreateItemInterface) : AppCompatDialogFragment() {

    private var editDistance: TextInputLayout? = null
    private var editTime: TextInputLayout? = null

    private var createItemInterface: CreateItemInterface = createItemInterface

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = activity?.let { AlertDialog.Builder(it) }!!
        val inflater: LayoutInflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.layout_dialog, null)


        builder.setView(view)
            .setTitle("Add new Jog")
            .setPositiveButton("Save") { _, _ ->
                val distanceStr = editDistance?.editText?.text.toString()
                val timeStr = editTime?.editText?.text.toString()
                createItemInterface.addJog?.invoke(distanceStr.toFloat(), timeStr.toInt())
            }.setNegativeButton("cancel") { _, _ ->

            }
        editDistance = view.findViewById(R.id.text_input_distance)
        editTime = view.findViewById(R.id.text_input_time)
        return builder.create()
    }


}

interface CreateItemInterface {
    var addJog: ((Float, Int) -> Unit)?
}