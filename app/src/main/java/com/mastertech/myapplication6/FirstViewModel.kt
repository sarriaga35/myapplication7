package com.mastertech.myapplication6

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.io.IOException

class FirstViewModel () :ViewModel(), DefaultLifecycleObserver {

    private val TAG = "FirstViewModel"


    override fun onCleared() {
        super.onCleared()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(TAG, "onCreate")
    }


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume")

    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(TAG, "onStart")


    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(TAG, "onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy")
    }

}
