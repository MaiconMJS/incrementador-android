package com.newoverride.incrementador.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.newoverride.incrementador.model.IncrementModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun incrementHook(): IncrementModel {
    val n1 = remember { mutableIntStateOf(0) }
    val n2 = remember { mutableIntStateOf(0) }

    val manualButtonPressed = remember { mutableStateOf(false) }
    val autoButtonPressed = remember { mutableStateOf(false) }
    val stopButtonPressed = remember { mutableStateOf(false) }
    val zeroButtonPressed = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val coroutineScopeAnimateButton = rememberCoroutineScope()

    val jobRef = remember { mutableStateOf<Job?>(null) }

    fun animateButton(active: MutableState<Boolean>) {
        active.value = true
        coroutineScopeAnimateButton.launch {
            delay(100)
            active.value = false
        }
    }

    val manualIncrement = {
        if (jobRef.value == null || !jobRef.value!!.isActive) {
            animateButton(manualButtonPressed)
            val job = coroutineScope.launch {
                delay(1000)
                n1.intValue++
            }
            jobRef.value = job
        }
    }

    val autoIncrement = {
        if (jobRef.value == null || !jobRef.value!!.isActive) {
            animateButton(autoButtonPressed)
            val job = coroutineScope.launch {
                while (isActive) {
                    delay(1000)
                    n1.intValue++
                }
            }
            jobRef.value = job
        }
    }

    val stopIncrement = {
        if (jobRef.value != null && jobRef.value!!.isActive) {
            animateButton(stopButtonPressed)
            jobRef.value?.cancel()
            jobRef.value = null
        }
    }

    val zeroDisplay = {
        if (n1.intValue > 0 || n2.intValue > 0) {
            animateButton(zeroButtonPressed)
            n1.intValue = 0
            n2.intValue = 0
        }
        if (jobRef.value != null && jobRef.value!!.isActive) {
            jobRef.value?.cancel()
            jobRef.value = null
        }
    }

    LaunchedEffect(n1.intValue) {
        delay(500)
        if (n2.intValue > 0) n2.intValue--
        n2.intValue += n1.intValue
    }

    // Encerra o temporizador quando o app é fechado >> Evitar vazamento de memória
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver {_, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                jobRef.value?.cancel()
                jobRef.value = null
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return IncrementModel(
        n1 = n1.intValue,
        n2 = n2.intValue,
        manualButtonPressed = manualButtonPressed.value,
        autoButtonPressed = autoButtonPressed.value,
        stopButtonPressed = stopButtonPressed.value,
        zeroButtonPressed = zeroButtonPressed.value,
        manualIncrement = manualIncrement,
        autoIncrement = autoIncrement,
        stopIncrement = stopIncrement,
        zeroDisplay = zeroDisplay
    )
}