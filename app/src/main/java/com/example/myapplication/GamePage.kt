package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class GamePage: AppCompatActivity(), View.OnClickListener {

    lateinit var rounds: TextView
    lateinit var time: TextView
    private val sequence: MutableList<Int> = ArrayList()
    private lateinit var buttons: List<Button>
    var running: Boolean = false
    
    val mHandler: Handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //init the round and time fields, should update them as the game goes, but idk
        //better figure out how to track time in kotlin

        buttons = listOf(
            findViewById(R.id.one),
            findViewById(R.id.two),
            findViewById(R.id.three),
            findViewById(R.id.four),
            findViewById(R.id.five),
            findViewById(R.id.six),
            findViewById(R.id.seven),
            findViewById(R.id.eight),
            findViewById(R.id.nine)
        )
        for (i in 0..8) {
            buttons[i].setOnClickListener(this)
            buttons[i].tag = i

        }

        rounds = findViewById(R.id.rounds)
        time = findViewById(R.id.time)

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

    }

    override fun onClick(v: View?) {
        playSequence()
        //TODO
    }

    private fun playSequence() {
        if (!running) {
            // locks the user out of clicking
            running = true
            mHandler.postDelayed({
                running = false
            }, (1000 * sequence.size + 1000).toLong())

            sequence.add(randomButton())

            for (i in 0 until sequence.size) {
                mHandler.postDelayed({
                    buttons[sequence[i]].setBackgroundColor(Color.YELLOW)
                }, (1000 * i).toLong())

                mHandler.postDelayed({
                    buttons[sequence[i]].setBackgroundColor(Color.BLUE)
                }, (1000 * i + 1000).toLong())
            }
        }
    }

    private fun randomButton() : Int {
        val r = Random()
        return r.nextInt(buttons.size)
    }

    fun lose(){
        //will jump to the loss screen, not really there yet but just call this and ill update this function later
    }

    companion object {
        private const val TAG = "Corsi-Tapping"
    }

}