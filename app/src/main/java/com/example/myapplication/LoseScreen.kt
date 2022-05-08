package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoseScreen : AppCompatActivity() {
    lateinit var home: Button
    lateinit var round: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose)

        round = findViewById(R.id.endround)
        var bundle: Bundle? = getIntent().getExtras()
        var score: Int? = bundle?.getInt("score")
        round.text = score.toString()

        home = findViewById(R.id.button2)
        home.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}
