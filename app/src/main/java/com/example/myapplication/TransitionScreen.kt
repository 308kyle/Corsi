package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TransitionScreen : AppCompatActivity()  {
    lateinit var next: Button
    lateinit var roundxnum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.transition_screen)

        next = findViewById(R.id.next)
        //change this to be the
        roundxnum = findViewById(R.id.roundxnum)
        var bundle: Bundle? = getIntent().getExtras()
        var duration: Long? = bundle?.getLong("1")
        var round: Int? = bundle?.getInt("2")

        roundxnum.setText("Round $round Cleared")
        next.setOnClickListener{
            val intent = Intent(this,GamePage::class.java)
            //add a bunch of intents extras to go back to game screen and have it functional
            intent.putExtra("1",duration)
            intent.putExtra("2",round)
            startActivity(intent)
            finish()
        }

    }
}