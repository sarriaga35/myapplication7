package com.mastertech.myapplication6

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FirstScreen (viewModel: FirstViewModel) {
    viewModel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)

Text(text = "Hola Mayra")
}