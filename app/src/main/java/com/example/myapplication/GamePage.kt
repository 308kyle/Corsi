package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.rgb
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import java.util.*
import kotlin.concurrent.timerTask

class GamePage: AppCompatActivity(), View.OnClickListener {

    private lateinit var rounds: TextView
    lateinit var time: TextView

    private val sequence: MutableList<Int> = ArrayList()
    private lateinit var buttons: List<Button>
    private var playerTurn: Boolean = false
    private var round: Int = 0
    private var count: Int = 0
    private var lives: Int = 1

    var duration: Long = 0
    var startTime: Long = 0
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    protected lateinit var mSharedP: SharedPreferences

    private val LRED = rgb(255, 204, 203)
    private val LGRN = rgb(144, 238, 144)

    private lateinit var mConstraintLayout: ConstraintLayout
    private var mDisplayWidth: Int = 0
    private var mDisplayHeight: Int = 0
    private var size: Int = 0
    private lateinit var mColors1: Array<ColorDrawable>
    private lateinit var mColors2: Array<ColorDrawable>
    private lateinit var mTransition1: TransitionDrawable
    private lateinit var mTransition2: TransitionDrawable

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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_game)

        mConstraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        mColors1 = arrayOf(ColorDrawable(LRED), ColorDrawable(Color.parseColor("#97cfff")))
        mColors2 = arrayOf(ColorDrawable(LGRN), ColorDrawable(Color.parseColor("#97cfff")))
        mTransition1 = TransitionDrawable(mColors1)
        mTransition2 = TransitionDrawable(mColors2)

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

        //val displayMetrics = DisplayMetrics()
//        mDisplayWidth = mConstraintLayout.width
//        mDisplayHeight = mConstraintLayout.height
        mDisplayWidth = resources.displayMetrics.widthPixels
        mDisplayHeight = resources.displayMetrics.heightPixels


        size = buttons[0].width
        val density = resources.displayMetrics.density
        mConstraintLayout.apply {
            //mConstraintLayout.
        }

        launchNewAttempt()

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
        mHandler.postDelayed({
            playSequence()
            round++
            rounds.text = round.toString()
        }, 1000)
    }

    override fun onClick(v: View?) {
        // locks the user out of clicking during the playback
        if (!playerTurn) {
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
                winAnimation()
                round++
                rounds.text = round.toString()
                count = 0
                lives = 1
                sequence.add(randomNoDoubles())
                playSequence()
            }
        } else if (lives > 0) {
            // lost a life
            loseAnimation()
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
            loseAnimation()
            buttons[index].setBackgroundColor(Color.RED)
            playerTurn = false
            lose()
        }
        Log.i(TAG, playerTurn.toString())
    }

    private fun playSequence() {
        Log.i(TAG, "new sequence")

        playerTurn = false
        mHandler.postDelayed({
            playerTurn = true
        }, (800 * (sequence.size + 1)).toLong())

        for (i in 0 until sequence.size) {
            mHandler.postDelayed({
                buttons[sequence[i]].setBackgroundColor(Color.YELLOW)
            }, (800 * (i + 1)).toLong())

            mHandler.postDelayed({
                buttons[sequence[i]].setBackgroundColor(Color.BLUE)
            }, (800 * (i + 2)).toLong())
        }
        Log.i(TAG, playerTurn.toString())
    }

    // random int 0-8
    private fun randomButton() : Int {
        val r = Random()
        return r.nextInt(buttons.size)
    }

    // random int 0-8 (excluding the previous int)
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

//    private fun placeButtons() {
//        val r = Random()
//
//        for (button in buttons) {
//            val lp = button.layoutParams as ConstraintLayout
//            val left = (r.nextFloat() * (mDisplayWidth - size)).toInt()
//            val top = (r.nextFloat() * (mDisplayHeight - size)).toInt()
//            //layoutParams.width = left
//            //layoutParams.height = top
//            //button.layoutpara
//
//            button.updateLayoutParams {
//                this.width = left
//                this.height = top
//            }
//        }
//    }

    private fun launchNewAttempt() {

        val r = Random()

        var list = ArrayList<Pair<Float, Float>>()
        for (btn in buttons) {
            var good: Boolean = false
            var left: Float = 0.toFloat()
            var top: Float = 0.toFloat()
            while (!good) {
                left = (r.nextFloat() * (mDisplayWidth - (2*btn.width)))
                top = (r.nextFloat() * (mDisplayHeight - (2*btn.height)))
                good = true
                for (b in list) {
                    if (overlap(Pair(b.first, b.second), Pair(btn.x, btn.y))) {
                        good = false
                    }
                }
            }
            Log.i(TAG, "$left, $top")
            btn.translationX = left
            btn.translationY = top
            list.add(Pair(btn.x, btn.y))
        }
        Log.i(TAG, "done")

    }

    // first is x, second is y
    private fun overlap(p1: Pair<Float, Float>, p2: Pair<Float, Float>): Boolean {
        val ret = (p2.first + size > p1.second &&
                p2.second + size > p1.second &&
                p1.first + size > p2.first &&
                p1.second + size > p2.second)
        Log.i(TAG, "OVERLAP $ret")
        return ret
    }

    private fun wAnimation() {
        mConstraintLayout.background = mTransition2
        mTransition2.startTransition(500)
    }

    private fun lAnimation() {
        mConstraintLayout.background = mTransition1
        mTransition1.startTransition(500)
    }

    private fun lose(){
        timer.cancel()
        save()
        val intent = Intent(this,LoseScreen::class.java)
        intent.putExtra("score", round)
        intent.putExtra("timer", timeConvert(duration))
        startActivity(intent)
        finish()
    }

    //corrects inaccuracies in the duration variable and starts the timer
    private fun timerStarter(t: CountDownTimer){
        //duration = System.currentTimeMillis() - startTime
        t.cancel()
        t.start()
    }

    fun timeConvert(milli: Long): String {
        val out: String
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

    private fun loseAnimation() {
        mConstraintLayout.background = mTransition1
        mTransition1.startTransition(500)
    }

    private fun winAnimation() {
        mConstraintLayout.background = mTransition2
        mTransition2.startTransition(500)
    }

    companion object {
        private const val TAG = "Corsi-Tapping"
        private const val TIMER_REFRESH: Long = 5000
        private const val TIMER_INTERVAL: Long = 100
        const val PREF_NAME: String = "my_pref"
        const val NAME: String = "name_key"
    }
}
