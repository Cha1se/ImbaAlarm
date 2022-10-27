package com.example.imbaalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        title = findViewById(R.id.timeAlarm)

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
            var minStr = "0${hour.toString()}"
        } else {
            minStr = min.toString()
        }
        if (min.toInt() == 0) {
            minStr = "00"
        }
        title.text = "$hourStr:$minStr"

    }
}