package com.newoverride.incrementador.model

import com.newoverride.incrementador.enum.ButtonType

data class IncrementModel(
    val number: MutableMap<String, Int>,
    val buttonPressedState: MutableMap<ButtonType, Boolean>,
    val manualIncrement: () -> Unit,
    val autoIncrement: () -> Unit,
    val stopIncrement: () -> Unit,
    val zeroDisplay: () -> Unit
)