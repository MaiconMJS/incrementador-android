package com.newoverride.incrementador.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.newoverride.incrementador.enum.ButtonType
import com.newoverride.incrementador.enum.NumberType
import com.newoverride.incrementador.model.IncrementModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun incrementHook(): IncrementModel {
    val number = remember { mutableStateMapOf(NumberType.N1 to 0, NumberType.N2 to 0) }

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

    fun manualIncrementOrAutoIncrement(
        buttonType: ButtonType,
        buttonPressedState: SnapshotStateMap<ButtonType, Boolean>,
        auto: Boolean
    ) {
        if (jobRef.value == null || !jobRef.value!!.isActive) {
            if (auto) {
                animateButton(buttonType, buttonPressedState)
                val job = coroutineScope.launch {
                    while (isActive) {
                        delay(1000)
                        number[NumberType.N1] = (number[NumberType.N1] ?: 0) + 1
                    }
                }
                jobRef.value = job
            } else {
                animateButton(buttonType, buttonPressedState)
                val job = coroutineScope.launch {
                    delay(1000)
                    number[NumberType.N1] = (number[NumberType.N1] ?: 0) + 1
                    jobRef.value = null
                }
                jobRef.value = job
            }
        }
    }

    val manualIncrement = {
        manualIncrementOrAutoIncrement(ButtonType.MANUAL, buttonPressedState, auto = false)
    }

    val autoIncrement = {
        manualIncrementOrAutoIncrement(ButtonType.AUTO, buttonPressedState, auto = true)
    }

    fun stopIncrementOrZeroIncrement(
        buttonType: ButtonType,
        buttonPressedState: SnapshotStateMap<ButtonType, Boolean>
    ) {
        if (jobRef.value != null && jobRef.value!!.isActive) {
            animateButton(buttonType, buttonPressedState)
            jobRef.value?.cancel()
            jobRef.value = null
        }
    }

    val stopIncrement = {
        stopIncrementOrZeroIncrement(ButtonType.STOP, buttonPressedState)
    }

    val zeroDisplay = {
        if ((number[NumberType.N1] ?: 0) > 0 || ((number[NumberType.N2] ?: 0) > 0)) {
            animateButton(ButtonType.ZERO, buttonPressedState)
            number[NumberType.N1] = 0
            number[NumberType.N2] = 0
        }
        stopIncrementOrZeroIncrement(ButtonType.ZERO, buttonPressedState)
    }

    LaunchedEffect(number[NumberType.N1]) {
        delay(500)

        val currentN1 = number[NumberType.N1] ?: 0
        val currentN2 = number[NumberType.N2] ?: 0

        if (currentN2 > 0) number[NumberType.N2] = currentN2 - 1
        number[NumberType.N2] = (number[NumberType.N2] ?: 0) + currentN1
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
        buttonPressedState = buttonPressedState,
        manualIncrement = manualIncrement,
        autoIncrement = autoIncrement,
        stopIncrement = stopIncrement,
        zeroDisplay = zeroDisplay
    )
}