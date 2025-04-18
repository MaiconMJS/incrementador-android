package com.newoverride.incrementador.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
    val number = remember { mutableStateMapOf("n1" to 0, "n2" to 0) }

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
                number["n1"] = (number["n1"]?: 0) + 1
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
                    number["n1"] = (number["n1"]?: 0) + 1
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
        if ((number["n1"] ?: 0) > 0 || ((number["n2"] ?: 0) > 0)) {
            animateButton(ButtonType.ZERO, buttonPressedState)
            number["n1"] = 0
            number["n2"] = 0
        }
        if (jobRef.value != null && jobRef.value!!.isActive) {
            animateButton(ButtonType.ZERO, buttonPressedState)
            jobRef.value?.cancel()
            jobRef.value = null
        }
    }

    LaunchedEffect(number["n1"]) {
        delay(500)

        val currentN1 = number["n1"] ?: 0
        val currentN2 = number["n2"] ?: 0

        if (currentN2 > 0) number["n2"] = currentN2 - 1
        number["n2"] = (number["n2"] ?: 0) + currentN1
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
        number = number,
        buttonPressedState,
        manualIncrement = manualIncrement,
        autoIncrement = autoIncrement,
        stopIncrement = stopIncrement,
        zeroDisplay = zeroDisplay
    )
}