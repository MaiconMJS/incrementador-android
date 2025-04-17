package com.newoverride.incrementador.model

import com.newoverride.incrementador.enum.ButtonType

data class IncrementModel(
    val n1: Int,
    val n2: Int,
    val buttonPressedState: MutableMap<ButtonType, Boolean>,
    val manualIncrement: () -> Unit,
    val autoIncrement: () -> Unit,
    val stopIncrement: () -> Unit,
    val zeroDisplay: () -> Unit
)