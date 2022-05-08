package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import java.util.*
import kotlin.concurrent.timerTask


class GamePage: AppCompatActivity(), View.OnClickListener {

    lateinit var rounds: TextView
    lateinit var time: TextView
    private val sequence: MutableList<Int> = ArrayList()
    private lateinit var buttons: List<Button>
    var playerTurn: Boolean = false
    private var running: Boolean = false
    private var round: Int = 0
    private var count: Int = 0
    private var lives: Int = 0
    var duration: Long = 0
    var startTime:Long = 0
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    protected lateinit var mSharedP: SharedPreferences

    //updates the ingame timer
    private val timer = object: CountDownTimer(TIMER_REFRESH, TIMER_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            duration = System.currentTimeMillis() - startTime
            time.setText(timeConvert(duration))
        }
        override fun onFinish() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //init the round and time fields, should update them as the game goes, but idk
        //better figure out how to track time in kotlin

        rounds = findViewById(R.id.rounds)
        time = findViewById(R.id.time)

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

        // maybe add button to start game?
        // starting game
        startTime = System.currentTimeMillis()
        var eventTimer = Timer()
        eventTimer.schedule(timerTask {
            timerStarter(timer)
        }, 0, TIMER_REFRESH)

        sequence.add(randomButton())
        playSequence()
        round++
        rounds.text = round.toString()
    }

    override fun onClick(v: View?) {

        // locks the user out of clicking during the playback
        if (!playerTurn || running) {
            return
        }
        val index: Int = v!!.tag as Int

        Log.i(TAG, "count: $count round: $round")
        Log.i(TAG, "" + sequence[count] + " " + index)

        // correct
        if (sequence[count] == index) {
            buttons[index].setBackgroundColor(Color.YELLOW)
            mHandler.postDelayed({
                buttons[index].setBackgroundColor(Color.BLUE)
            }, 200)
            count++
            if (count == round) {
                // next round
                round++
                rounds.text = round.toString()
                count = 0
                lives = 1
                sequence.add(randomNoDoubles())
                playSequence()

            }
        } else if (lives > 0) {
            // lost a life
            buttons[index].setBackgroundColor(Color.RED)
            mHandler.postDelayed({
                buttons[index].setBackgroundColor(Color.BLUE)
            }, 200)
            count = 0
            lives--
            playSequence()
        } else {
            // game over
            // temp code
            buttons[index].setBackgroundColor(Color.RED)
            playerTurn = false
            lose()
        }
        Log.i(TAG, playerTurn.toString())
    }

    private fun playSequence() {
        Log.i(TAG, "new sequence")
        running = true
        mHandler.postDelayed({
            playerTurn = true
            running = false
        }, (800 * (sequence.size + 1)).toLong())

        for (i in 0 until sequence.size) {
            mHandler.postDelayed({
                buttons[sequence[i]].setBackgroundColor(Color.YELLOW)
            }, (800 * (i+1)).toLong())

            mHandler.postDelayed({
                buttons[sequence[i]].setBackgroundColor(Color.BLUE)
            }, (800 * (i+2)).toLong())
        }
        Log.i(TAG, playerTurn.toString())
    }

    private fun randomButton() : Int {
        val r = Random()
        return r.nextInt(buttons.size)
    }

    private fun randomNoDoubles() : Int {
        val r = Random()
        var rand = r.nextInt(buttons.size - 1)
        if (sequence[sequence.size - 1] > rand) {
            return rand
        } else {
            rand++
        }
        return rand
    }

    private fun lose(){
        save()
        val intent = Intent(this,LoseScreen::class.java)
        intent.putExtra("score", round)
        startActivity(intent)
    }

    //corrects inaccuracies in the duration variable and starts the timer
    private fun timerStarter(t: CountDownTimer){
        //duration = System.currentTimeMillis() - startTime
        t.cancel()
        t.start()
    }

    fun timeConvert(milli: Long): String {
        var out = ""
        if (milli >= 3600000){
            val hours = milli / 1000 / 60 / 60
            val minutes = milli / 1000 / 60 % 60
            val seconds = milli / 1000 % 60 % 60
            out = timeFormat(hours) + ":"+ timeFormat(minutes) + ":" + timeFormat(seconds)
        }
        else if (milli >= 60000){
            val minutes = milli / 1000 / 60
            val seconds = milli / 1000 % 60
            out = timeFormat(minutes) + ":" + timeFormat(seconds)
        }
        else{
            val seconds = milli / 1000 % 60
            out = "00:" + timeFormat(seconds)
        }

        return out
    }

    fun timeFormat(t: Long): String{
        if (t<10){
            return "0$t"
        }
        else{
            return "$t"
        }
    }
    
    fun save() {
        mSharedP = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val savedScore = mSharedP.getInt(NAME, 0)
        // save if the current score is the higher than the saved score
        if (savedScore < round) {
            val edit = mSharedP.edit()
            edit.putInt(NAME, round)
            edit.commit()
        }
    }

//    fun get() {
//        mSharedP = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
//        name.text = sharedpreferences.getString(NAME, "")
//    }


    companion object {
        private const val TAG = "Corsi-Tapping"
        private const val TIMER_REFRESH: Long = 5000
        private const val TIMER_INTERVAL: Long = 100
        private const val PREF_NAME: String = "my_pref"
        private const val NAME: String = "name_key"

    }
}