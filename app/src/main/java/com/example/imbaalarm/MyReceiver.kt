package com.example.imbaalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.core.content.ContextCompat.registerReceiver

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val randomIntent = Intent(context.applicationContext, AlarmActivity::class.java)
        context.startActivity(randomIntent)
    }
}