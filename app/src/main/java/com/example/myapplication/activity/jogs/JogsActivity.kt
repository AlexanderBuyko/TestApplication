package com.example.myapplication.activity.jogs

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.myapplication.R
import com.example.myapplication.adapter.OnJogListener
import com.example.myapplication.adapter.RecycleAdapter
import com.example.myapplication.api.APIBuilder
import com.example.myapplication.api.DataAPI
import com.example.myapplication.api.models.Jog.Jog
import com.example.myapplication.dialog.CreateItemDialog
import com.example.myapplication.dialog.CreateItemInterface
import com.example.myapplication.dialog.EditItemDialog
import com.example.myapplication.dialog.EditItemInterface
import com.example.myapplication.help.dateString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.util.*

class JogsActivity : AppCompatActivity(), OnJogListener, CreateItemInterface, EditItemInterface {

    companion object {
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val BEARER = "BEARER"
        const val MINIMUM_DISTANCE = 0
        const val MAXIMUM_DISTANE = 10000000

    }

    val comspositDisposable = CompositeDisposable()

    private var verticalLinearLayoutManager: LinearLayoutManager? = null

    private var adapter: RecycleAdapter? = null
    private var recycleView: RecyclerView? = null

    private var jogs: ArrayList<Jog> = arrayListOf()

    override var addJog: ((Float, Int) -> Unit)? = null
    override var editJog: ((Float, Int, Jog) -> Unit)? = null
    override var deleteJog: ((Jog) -> Unit)? = null

    private var distanceFromEditText: EditText? = null
    private var distanceToEditText: EditText? = null

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jogs_activity)

        configureEditTextViews()
        configureAccessToken()
        configureClosures()
        configureFloatyButton()
        fetchJogs()
    }

    private fun configureEditTextViews() {
        distanceFromEditText = findViewById(R.id.from_distance)
        distanceToEditText = findViewById(R.id.to_distance)

        distanceFromEditText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(8))
        distanceToEditText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(8))

        val listener: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                distanceFilterChanged()
            }
        }

        distanceFromEditText?.addTextChangedListener(listener)
        distanceToEditText?.addTextChangedListener(listener)
    }

    private fun configureAccessToken() {
        val accessToken = intent.getStringExtra(EXTRA_TOKEN)
        val tokenType = intent.getStringExtra(BEARER)
        DataAPI.accessToken = tokenType.capitalize().plus(" $accessToken")
    }

    private fun configureClosures() {
        addJog = { distance, time ->
            AsyncTask.execute {
                comspositDisposable.add(
                    APIBuilder.instance()
                        .create(DataAPI::class.java)
                        .createJog(DataAPI.accessToken, LocalDateTime.now().dateString(), time, distance)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            this.jogs.add(it.data)
                            adapter?.addItem(it.data)
                            distanceFilterChanged()
                        }, Throwable::printStackTrace)
                )
            }
        }

        deleteJog = { jog ->
            AsyncTask.execute {
                comspositDisposable.add(
                    APIBuilder.instance()
                        .create(DataAPI::class.java)
                        .deleteJog(DataAPI.accessToken, jog.id, jog.user_id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            jogs.remove(jog)
                            adapter?.deleteItem(jog)
                        }, Throwable::printStackTrace)
                )
            }

        }

        editJog = { distance: Float, time: Int, jog: Jog ->
            AsyncTask.execute {
                comspositDisposable.add(
                    APIBuilder.instance()
                        .create(DataAPI::class.java)
                        .updateJog(
                            DataAPI.accessToken,
                            jog.id,
                            jog.user_id,
                            LocalDateTime.now().dateString(),
                            time,
                            distance
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            jogs.replaceAll { item ->
                                Unit
                                if (item.id == jog.id && item.user_id == jog.user_id) {
                                    it.data
                                } else {
                                    item
                                }
                            }
                            distanceFilterChanged()
                        }, Throwable::printStackTrace)
                )
            }
        }
    }

    private fun configureFloatyButton() {
        val floatingActionButtin = findViewById<FloatingActionButton>(R.id.fab)
        floatingActionButtin.setOnClickListener(View.OnClickListener {
            onAddButtonClick()
        })
    }

    private fun onAddButtonClick() {
        val newItemDialog = CreateItemDialog(this)
        newItemDialog.show(supportFragmentManager, "dialog")
    }

    private fun fetchJogs() {
        comspositDisposable.addAll(
            APIBuilder.instance()
                .create(DataAPI::class.java)
                .getJogs(DataAPI.accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    verticalLinearLayoutManager = LinearLayoutManager(this)

                    recycleView = findViewById(R.id.recycler)
                    recycleView?.layoutManager = verticalLinearLayoutManager
                    recycleView?.setHasFixedSize(true)

                    adapter = RecycleAdapter(this)
                    recycleView?.adapter = adapter

                    jogs = it.data.jogs
                    adapter?.setItems(jogs.reversed())
                }, Throwable::printStackTrace)
        )
    }

    override fun onJogClick(position: Int) {
        val editItemDialog = EditItemDialog(adapter?.getItem(position)!!, this)
        editItemDialog.show(supportFragmentManager, "dialog")
    }

    private fun distanceFilterChanged() {
        adapter?.setItems(jogs.filter {
            val lowerBound =
                if (distanceFromEditText?.text.isNullOrEmpty()) MINIMUM_DISTANCE else distanceFromEditText?.text.toString().toInt()
            val upperBound =
                if (distanceToEditText?.text.isNullOrEmpty()) MAXIMUM_DISTANE else distanceToEditText?.text.toString().toInt()
            it.distance >= lowerBound && it.distance <= upperBound
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        comspositDisposable.clear()
    }
}