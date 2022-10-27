package com.example.imbaalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.junit.Test
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timerTask

public lateinit var mainLay: ConstraintLayout
public lateinit var titleContainer: ConstraintLayout
public lateinit var scrollContainer: ScrollView
public lateinit var contentLay: LinearLayout

public var IDLay: Int = 2000
public var IDText: Int = 3000
public var IDSwitch: Int = 4000

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLay = findViewById(R.id.mainLay)
        scrollContainer = findViewById(R.id.ScrollContainer)
        titleContainer = findViewById(R.id.title)
        contentLay = findViewById(R.id.ContentLayout)
    }

    fun AddAlarm (view: View) {

        val time1 = Calendar.getInstance()
        val hour1 = time1.get(Calendar.HOUR_OF_DAY)
        val min1 = time1.get(Calendar.MINUTE)

        var materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour1)
            .setMinute(min1)
            .setTitleText("Select time")
            .build()
        materialTimePicker.show(supportFragmentManager, "MainActivity")
        materialTimePicker.addOnPositiveButtonClickListener {
            var pickedHour = materialTimePicker.hour
            var pickedMin = materialTimePicker.minute
            /*if (materialTimePicker.hour < 10) {
                pickedHour = "0${materialTimePicker.hour}".toInt()
            }
            if (materialTimePicker.minute < 10) {
                pickedMin = "${materialTimePicker.minute}0".toInt()
            }*/
            AddAlarmMessage(pickedHour.toString(), pickedMin.toString())

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                materialTimePicker.hour,
                materialTimePicker.minute,
                0
            )
            setAlarm(calendar.timeInMillis)
            println("\n${calendar.get(Calendar.YEAR)}\n${calendar.get(Calendar.MONTH)}\n${calendar.get(Calendar.DAY_OF_MONTH)}\n${calendar.get(Calendar.HOUR_OF_DAY)}\n${calendar.get(Calendar.MINUTE)}\n${calendar.get(Calendar.SECOND)}")
        }
//            var cal: Calendar = Calendar.getInstance()
//            cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), pickedHour, pickedMin, Calendar.getInstance().get(Calendar.SECOND))
    }

    fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }

    fun AddAlarmMessage (hour: String, min: String) {
        val jetBrainsFont: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.jetbrains_mono_medium)

        var linerLay: RelativeLayout = RelativeLayout(this)
        var alarmSwitch: SwitchMaterial = SwitchMaterial(this)

        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = dpToPx(10)

        linerLay.apply {
            id = IDLay++
            setBackgroundColor(Color.parseColor("#494949"))
            layoutParams = params
        }

        contentLay.addView(linerLay)
        var paramparam: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramparam.addRule(RelativeLayout.CENTER_HORIZONTAL)
        paramparam.addRule(RelativeLayout.CENTER_VERTICAL)

        var alarmText: TextView = TextView(this)
        alarmText.apply {
            id = IDText++
            inputType = EditText.AUTOFILL_TYPE_TEXT
            text = "$hour:$min"
            textAlignment = EditText.TEXT_ALIGNMENT_CENTER
            textSize = dpToPx(10).toFloat()
            setTextColor(ContextCompat.getColor(context,R.color.white))
            typeface = jetBrainsFont
            layoutParams = paramparam
        }

        var paramSwitch: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramSwitch.addRule(RelativeLayout.ALIGN_RIGHT)
        paramSwitch.addRule(RelativeLayout.ALIGN_END)
        paramSwitch.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        paramSwitch.addRule(RelativeLayout.CENTER_VERTICAL)

        alarmSwitch.apply {
            id = IDSwitch++
            layoutParams = paramSwitch
            check(true)
        }
        linerLay.addView(alarmSwitch)
        linerLay.addView(alarmText)

    }

    fun dpToPx(dp: Int): Int {
        var displayMetrics : DisplayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}