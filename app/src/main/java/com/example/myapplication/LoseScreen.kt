package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoseScreen : AppCompatActivity() {
    lateinit var home: Button
    lateinit var round: TextView
    lateinit var highscore: TextView
    lateinit var timeView: TextView
    protected lateinit var mSharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose)

        round = findViewById(R.id.endround)
        var bundle: Bundle? = getIntent().getExtras()
        var score: Int? = bundle?.getInt("score")
        round.text = score.toString()

        highscore = findViewById(R.id.highscore)
        get()

        timeView = findViewById(R.id.timer)

        var time: String? = bundle?.getString("timer")
        timeView.text = time

        home = findViewById(R.id.button2)
        home.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun get() {
        mSharedPreferences = getSharedPreferences(GamePage.PREF_NAME, MODE_PRIVATE)
        highscore.text = mSharedPreferences.getInt(GamePage.NAME, 0).toString()
    }
}
