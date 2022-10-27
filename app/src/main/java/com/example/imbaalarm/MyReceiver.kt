package com.example.imbaalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var mainactivity = MainActivity()
        // This method
        // is called when the BroadcastReceiver is receiving an Intent broadcast.
        val randomIntent = Intent(context.applicationContext, AlarmActivity::class.java)
       context.startActivity(randomIntent)
    }
}