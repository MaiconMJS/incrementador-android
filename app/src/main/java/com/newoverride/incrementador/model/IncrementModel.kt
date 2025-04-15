package com.newoverride.incrementador.model

data class IncrementModel(
    val n1: Int,
    val n2: Int,
    val manualButtonPressed: Boolean,
    val autoButtonPressed: Boolean,
    val stopButtonPressed: Boolean,
    val zeroButtonPressed: Boolean,
    val manualIncrement: () -> Unit,
    val autoIncrement: () -> Unit,
    val stopIncrement: () -> Unit,
    val zeroDisplay: () -> Unit
)