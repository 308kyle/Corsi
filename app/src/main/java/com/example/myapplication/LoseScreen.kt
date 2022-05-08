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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose)

        round = findViewById(R.id.endround)
        var bundle: Bundle? = getIntent().getExtras()
        var score: Int? = bundle?.getInt("score")
        round.text = score.toString()

        highscore = findViewById(R.id.highscore)

        home = findViewById(R.id.button2)
        home.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {
        private const val TAG = "Corsi-Tapping"
        private const val TIMER_REFRESH: Long = 5000
        private const val TIMER_INTERVAL: Long = 100
        private const val PREF_NAME: String = "my_pref"
        private const val NAME: String = "name_key"

    }
}
