package com.example.myapplication.activity.onboarding

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.activity.jogs.JogsActivity
import com.example.myapplication.api.APIBuilder
import com.example.myapplication.api.AuthAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.onboarding_activity.*

class OnboardingActivity : AppCompatActivity() {

    val comspositDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity)

        button.setOnClickListener {
            comspositDisposable.add(APIBuilder.instance()
                .create(AuthAPI::class.java)
                .logIn("hello")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val intent = Intent(this, JogsActivity::class.java)
                    intent.putExtra(JogsActivity.EXTRA_TOKEN, it.data.access_token)
                    intent.putExtra(JogsActivity.BEARER, it.data.token_type)
                    startActivity(intent)
                }, Throwable::printStackTrace)
            )

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        comspositDisposable.clear()
    }
}