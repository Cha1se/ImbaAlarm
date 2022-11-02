package com.example.imbaalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity(){
    public lateinit var mainLay: ConstraintLayout
    public lateinit var titleContainer: ConstraintLayout
    public lateinit var scrollContainer: ScrollView
    public lateinit var contentLay: LinearLayout

    public var IDLay: Int = 2000
    public var IDText: Int = 3000
    public var IDSwitch: Int = 4000
    public var IDDelete: Int = 5000

    public var alarmTime: String = ""
    public var maxTimeInMills: Long = 0
    public var pickedHour: Int = 0
    public var pickedMin: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLay = findViewById(R.id.mainLay)
        scrollContainer = findViewById(R.id.ScrollContainer)
        titleContainer = findViewById(R.id.title)
        contentLay = findViewById(R.id.ContentLayout)
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
        registerReceiver(MyReceiver(), filter)

    }

    fun AddAlarm (view: View) {

        view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(Runnable {
            view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }).start()

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
            pickedHour = materialTimePicker.hour
            pickedMin = materialTimePicker.minute

            AddAlarmMessage(pickedHour.toString(), pickedMin.toString())

            setAlarm(pickedHour, pickedMin, true)
        }
//            var cal: Calendar = Calendar.getInstance()
//            cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), pickedHour, pickedMin, Calendar.getInstance().get(Calendar.SECOND))
    }

    fun setAlarm(hour: Int, min: Int , isActivate: Boolean) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        var calendar: Calendar = Calendar.getInstance()
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            hour,
            min,
            0
        )

        if (isActivate) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, checkBackTime(calendar).timeInMillis, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
        }


    }

    fun checkBackTime(cal: Calendar): Calendar {
        var nowCal = Calendar.getInstance()

        if (cal.get(Calendar.HOUR_OF_DAY) < nowCal.get(Calendar.HOUR_OF_DAY)) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)
            return cal
        } else if ((cal.get(Calendar.HOUR_OF_DAY) == nowCal.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.MINUTE) < nowCal.get(Calendar.MINUTE))) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)
            return cal
        } else if ((cal.get(Calendar.HOUR_OF_DAY) == nowCal.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.MINUTE) == nowCal.get(Calendar.MINUTE))) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)
            return cal
        } else {
            return cal
        }
    }

    fun AddAlarmMessage (hour: String, min: String) {
        val jetBrainsFont: Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.jetbrains_mono_medium)

        var alarmContainer: RelativeLayout = RelativeLayout(this)
        var alarmSwitch: SwitchMaterial = SwitchMaterial(this)
        var deleteAlarm: ImageButton = ImageButton(this)

        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(56))
        params.topMargin = dpToPx(10)

        alarmContainer.apply {
            id = IDLay++
            setBackgroundResource(R.drawable.alarm_block)
            layoutParams = params
        }

        contentLay.addView(alarmContainer)

        var paramparam: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramparam.addRule(RelativeLayout.CENTER_HORIZONTAL)
        paramparam.addRule(RelativeLayout.CENTER_VERTICAL)

        var alarmText: TextView = TextView(this)
        alarmText.apply {
            id = IDText++
            inputType = EditText.AUTOFILL_TYPE_TEXT
            text = "${correctTime(hour.toInt(), min.toInt())[0]}:${correctTime(hour.toInt(), min.toInt())[1]}"
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
        paramSwitch.marginEnd = dpToPx(15)

        alarmSwitch.apply {
            scaleX = 1.2f
            scaleY = 1.2f
            id = IDSwitch++
            layoutParams = paramSwitch
            isChecked = true
        }
        alarmSwitch.setOnClickListener(View.OnClickListener {
            if (!alarmSwitch.isChecked) {
                setAlarm(pickedHour, pickedMin, false)
            } else {
                setAlarm(pickedHour, pickedMin, true)
            }
        })

        var paramDelete: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramDelete.addRule(RelativeLayout.ALIGN_LEFT)
        paramDelete.addRule(RelativeLayout.ALIGN_END)
        paramDelete.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        paramDelete.addRule(RelativeLayout.CENTER_VERTICAL)
        deleteAlarm.apply {
            id = IDDelete++
            scaleType = ImageView.ScaleType.FIT_CENTER
            setImageResource(R.drawable.delete_ico)
            setBackgroundColor(Color.argb(0, 255,255,255))
            layoutParams = paramDelete
        }

        deleteAlarm.setOnClickListener(View.OnClickListener {
            alarmContainer.animate().alpha(0f)
                .translationX(-mainLay.width.toFloat())
                .setDuration(500L)
                .withEndAction(Runnable {
                    val idAlarm = deleteAlarm.id
                    setAlarm(pickedHour, pickedMin, false)
                    contentLay.removeView(findViewById(idAlarm-3000))
                })
                .start()
        })

        alarmContainer.addView(alarmSwitch)
        alarmContainer.addView(alarmText)
        alarmContainer.addView(deleteAlarm)
        alarmContainer.alpha = 0f
        alarmContainer.translationX = -mainLay.width.toFloat()
        alarmContainer.animate().alpha(1f)
            .translationX(0f)
            .setDuration(500L)
            .start()

/*        var nowCalendar = Calendar.getInstance()
        CoroutineScope(Dispatchers.Main + Job()).launch {
            var isOn = true
            while (isOn) {
                if (nowCalendar.get(Calendar.HOUR_OF_DAY) == hour.toInt() && nowCalendar.get(Calendar.MINUTE) == min.toInt()) {
                    alarmSwitch.isChecked = false
                }
                if (alarmContainer.childCount == 0) {
                    isOn = false
                }
                delay(100)
            }
        }*/

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