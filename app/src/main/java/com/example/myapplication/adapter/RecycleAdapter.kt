package com.example.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.api.models.Jog.Jog
import java.util.*
import kotlin.collections.ArrayList


interface OnJogListener {
    fun onJogClick(position: Int)
}

class RecycleAdapter(onJogListener: OnJogListener) : RecyclerView.Adapter<RecyclerViewHolder>() {


    private var items: ArrayList<Jog> = ArrayList()
    private var mOnJogListener: OnJogListener? = onJogListener

    fun setItems(fakeItems: List<Jog>) {
        items.apply {
            clear()
            addAll(fakeItems)
        }
        notifyDataSetChanged()
    }

    fun addItem(item: Jog) {
        items.add(0, item)
        notifyItemRangeChanged(0, itemCount)
    }

    fun getItem(index: Int): Jog {
        return items.get(index)

    }

    fun deleteItem(jog: Jog) {
        val index = items.indexOf(jog)
        items.remove(jog)
        notifyItemRemoved(index)
        notifyItemRangeChanged(0, itemCount, null)
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item, p0, false)

        return mOnJogListener?.let { RecyclerViewHolder(view, it) }!!
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: RecyclerViewHolder, p1: Int) {
        items.get(p1).let { p0.bind(it) }
    }
}

class RecyclerViewHolder : RecyclerView.ViewHolder, View.OnClickListener {

    private var distance: TextView
    private var time: TextView
    private var date: TextView
    private var speed: TextView
    private var onJogListener: OnJogListener? = null

    constructor(itemView: View, onJogListener: OnJogListener) : super(itemView) {
        distance = itemView.findViewById(R.id.distance)
        time = itemView.findViewById(R.id.time)
        date = itemView.findViewById(R.id.data)
        speed = itemView.findViewById(R.id.speed)
        this.onJogListener = onJogListener
        itemView.setOnClickListener(this)
    }

    fun bind(modelItem: Jog) {
        distance.setText("Distance: ".plus(modelItem.distance.toString()).plus(" m"))
        var currentTime = modelItem.time
        time.setText("Time: ".plus(currentTime.toString()).plus(" s"))
        val dateStr = if (modelItem.date.contains("-")) modelItem.date else Date(modelItem.date.toLong() * 1000).toString()
        date.setText("Date: ".plus(dateStr))
        val currentSpeed = ((modelItem.distance / currentTime.toDouble()) * 100).toInt().toDouble() / 100
        speed.setText("Speed: ".plus(currentSpeed).plus(" m/s"))
    }

    override fun onClick(v: View?) {
        onJogListener?.onJogClick(adapterPosition)
    }
}