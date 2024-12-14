package com.mastertech.myapplication6

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

@Composable
fun MainScreen() {
    val TAG="SecondScreen"

    ComposableLifecycle { source, event ->
        when(event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "onCreate")
            }
            Lifecycle.Event.ON_START -> {
                Log.d(TAG,"On Start")
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG,"On Resume")
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "onPause")
            }
            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG,"On Stop")
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG,"On Destroy")
            }
            else -> { Log.d(TAG,"Out of life cycle")}
        }
    }
}
