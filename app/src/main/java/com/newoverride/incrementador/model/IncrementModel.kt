package com.newoverride.incrementador.model

import com.newoverride.incrementador.enum.ButtonType
import com.newoverride.incrementador.enum.NumberType

data class IncrementModel(
    val number: MutableMap<NumberType, Int>,
    val buttonPressedState: MutableMap<ButtonType, Boolean>,
    val manualIncrement: () -> Unit,
    val autoIncrement: () -> Unit,
    val stopIncrement: () -> Unit,
    val zeroDisplay: () -> Unit
)