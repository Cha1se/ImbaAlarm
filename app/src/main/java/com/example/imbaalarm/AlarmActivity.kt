package com.example.imbaalarm

import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    lateinit var title: TextView
    lateinit var btn: ImageView
    var mMediaPlayer: MediaPlayer? = null

    private var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
//        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
        setContentView(R.layout.activity_alarm)

        var displayMetrics = DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

        title = findViewById(R.id.timeAlarm)
        btn = findViewById(R.id.alarmButton)

        playSound()
        animateAll()
        setTime()
    }
    fun animateAll () {
        title.alpha = 0f
        btn.alpha = 0f
        btn.translationY = height.toFloat()
        btn.animate().alpha(1f)
            .translationY(dpToPx(4).toFloat())
            .setDuration(1000)
            .withEndAction(Runnable {
                title.animate().alpha(1f).setDuration(200).start()
            }).start()
    }

    fun setTime() {
        var cal = Calendar.getInstance()

        var hour = cal.get(Calendar.HOUR_OF_DAY)
        var min = cal.get(Calendar.MINUTE)

        var hourStr: String = ""
        var minStr: String = ""

        if (hour.toInt() < 10) {
            hourStr = "0${hour.toString()}"
        } else {
            hourStr = hour.toString()
        }
        if (hour.toInt() == 0) {
            hourStr = "00"
        }

        if (min.toInt() < 10) {
            minStr = "0${min.toString()}"
        } else {
            minStr = min.toString()
        }
        if (min.toInt() == 0) {
            minStr = "00"
        }
        title.text = "${correctTime(hour, min)[0]}:${correctTime(hour, min)[1]}"
    }

    fun onStopAlarm (view: View) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        btn.animate().scaleY(5f)
            .scaleX(1.5f)
            .setDuration(500)
            .withEndAction { this.onBackPressed() }
            .start()
        title.animate().alpha(0f).setDuration(400).start()

    }

    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.jojo_awaken_3)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun dpToPx(dp: Int): Int {
        var displayMetrics : DisplayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    fun correctTime(hour: Int, min: Int):List<String> {
        var hourStr: String
        var minStr: String

        if (hour.toInt() < 10) {
            hourStr = "0${hour.toString()}"
        } else {
            hourStr = hour.toString()
        }
        if (hour.toInt() == 0) {
            hourStr = "00"
        }

        if (min.toInt() < 10) {
            minStr = "0${min.toString()}"
        } else {
            minStr = min.toString()
        }
        if (min.toInt() == 0) {
            minStr = "00"
        }
        return listOf(hourStr, minStr)
    }
}