package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var play: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play = findViewById(R.id.button)
        play.setOnClickListener{
            val intent = Intent(this,GamePage::class.java)
            startActivity(intent)
            //Toast.makeText(this,"test!",Toast.LENGTH_LONG).show()

        }

    }
}