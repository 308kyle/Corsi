package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TransitionScreen : AppCompatActivity()  {
    lateinit var next: Button
    lateinit var roundxnum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transition_screen)

        next = findViewById(R.id.next)
        //change this to be the
        roundxnum = findViewById(R.id.roundxnum)

        next.setOnClickListener{
            val intent = Intent(this,GamePage::class.java)
            //add a bunch of intents extras to go back to game screen and have it functional
            startActivity(intent)
        }

    }
}