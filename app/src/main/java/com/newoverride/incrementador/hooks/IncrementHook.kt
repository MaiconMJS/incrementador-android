package com.newoverride.incrementador.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.newoverride.incrementador.enum.ButtonType
import com.newoverride.incrementador.model.IncrementModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun incrementHook(): IncrementModel {
    val n1 = remember { mutableIntStateOf(0) }
    val n2 = remember { mutableIntStateOf(0) }

    val buttonPressedState = remember {
        mutableStateMapOf(
            ButtonType.MANUAL to false,
            ButtonType.AUTO to false,
            ButtonType.STOP to false,
            ButtonType.ZERO to false
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val coroutineScopeAnimateButton = rememberCoroutineScope()

    val jobRef = remember { mutableStateOf<Job?>(null) }

    fun animateButton(type: ButtonType, active: MutableMap<ButtonType, Boolean>) {
        active[type] = true
        coroutineScopeAnimateButton.launch {
            delay(100)
            active[type] = false
        }
    }

    val manualIncrement = {
        if (jobRef.value == null || !jobRef.value!!.isActive) {
            animateButton(ButtonType.MANUAL, buttonPressedState)
            val job = coroutineScope.launch {
                delay(1000)
                n1.intValue++
                jobRef.value = null
            }
            jobRef.value = job
        }
    }

    val autoIncrement = {
        if (jobRef.value == null || !jobRef.value!!.isActive) {
            animateButton(ButtonType.AUTO, buttonPressedState)
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
            animateButton(ButtonType.STOP, buttonPressedState)
            jobRef.value?.cancel()
            jobRef.value = null
        }
    }

    val zeroDisplay = {
        if (n1.intValue > 0 || n2.intValue > 0) {
            animateButton(ButtonType.ZERO, buttonPressedState)
            n1.intValue = 0
            n2.intValue = 0
        }
        if (jobRef.value != null && jobRef.value!!.isActive) {
            animateButton(ButtonType.ZERO, buttonPressedState)
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
        val observer = LifecycleEventObserver { _, event ->
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
        buttonPressedState,
        manualIncrement = manualIncrement,
        autoIncrement = autoIncrement,
        stopIncrement = stopIncrement,
        zeroDisplay = zeroDisplay
    )
}