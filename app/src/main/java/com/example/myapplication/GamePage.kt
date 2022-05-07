package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class GamePage: AppCompatActivity()  {
    lateinit var one: Button
    lateinit var two: Button
    lateinit var three: Button
    lateinit var four: Button
    lateinit var five: Button
    lateinit var six: Button
    lateinit var seven: Button
    lateinit var eight: Button
    lateinit var nine: Button
    lateinit var rounds: TextView
    lateinit var time:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //init the round and time fields, should update them as the game goes, but idk
        //better figure out how to track time in kotlin

        rounds = findViewById(R.id.rounds)
        time = findViewById(R.id.time)
        //initializing the buttons
        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)

//      all the on click listeners for the buttons are below (theres a lose function at the bottom)
//      in case it isnt obvious these listeners refer to these buttons
//              |     |
//           1  |  2  |  3
//         _____|_____|_____
//              |     |
//           4  |  5  |  6
//         _____|_____|_____
//              |     |
//           7  |  8  |  9
//              |     |
        one.setOnClickListener{

        }
        two.setOnClickListener{

        }
        three.setOnClickListener{

        }
        four.setOnClickListener{

        }
        five.setOnClickListener{

        }
        six.setOnClickListener{

        }
        seven.setOnClickListener{

        }
        eight.setOnClickListener{

        }
        nine.setOnClickListener{
            lose()
        }





    }
    fun lose(){
        //will jump to the loss screen, not really there yet but just call this and ill update this function later
    }
}