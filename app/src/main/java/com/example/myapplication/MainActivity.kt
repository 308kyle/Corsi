package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private lateinit var play: Button
    //private lateinit var animation:AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        play = findViewById(R.id.button)
        play.setOnClickListener{
            val intent = Intent(this,GamePage::class.java)
            startActivity(intent)
            //Toast.makeText(this,"test!",Toast.LENGTH_LONG).show()

        }
    }
}