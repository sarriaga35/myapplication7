package com.mastertech.myapplication6

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

@Composable
fun <viewModel : LifecycleObserver> viewModel.observeLifecycleEvents(lifecycle: Lifecycle) {
    DisposableEffect(key1 = lifecycle) {
            lifecycle.addObserver(this@observeLifecycleEvents)
        onDispose {
            lifecycle.removeObserver(this@observeLifecycleEvents)
        }
        
    }
}
    
