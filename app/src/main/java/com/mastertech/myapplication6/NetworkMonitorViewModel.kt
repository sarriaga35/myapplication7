package com.mastertech.myapplication6

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mastertech.myapplication6.Constants.Companion.TAG

class NetworkMonitorViewModel(application: Application): AndroidViewModel(application) {

    fun startService(){
        val context: Application = getApplication()
        context.startService(Intent(context,SerialService::class.java))
    }

    override fun onCleared() {
        super.onCleared()
        val context: Application = getApplication()
        context.stopService(Intent(context,SerialService::class.java))
        Log.d(TAG,"cleared")
    }
}