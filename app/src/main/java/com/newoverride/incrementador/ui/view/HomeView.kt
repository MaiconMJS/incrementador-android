package com.newoverride.incrementador.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.newoverride.incrementador.R
import com.newoverride.incrementador.dimens.Dimens
import com.newoverride.incrementador.hooks.incrementHook
import com.newoverride.incrementador.ui.components.ButtonStyle
import com.newoverride.incrementador.ui.components.Display
import com.newoverride.incrementador.ui.theme.Black
import com.newoverride.incrementador.ui.theme.Blue
import com.newoverride.incrementador.ui.theme.Green
import com.newoverride.incrementador.ui.theme.IncrementadorTheme
import com.newoverride.incrementador.ui.theme.Red

@Composable
fun HomeView() {

    val incrementHook = incrementHook()

    IncrementadorTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Display(incrementHook.n1, incrementHook.n2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ButtonStyle(
                        textButton = stringResource(R.string.ManualButton),
                        onClick = incrementHook.manualIncrement,
                        isPressed = incrementHook.manualButtonPressed,
                        colors = ButtonDefaults.buttonColors(containerColor = Blue)
                    )
                    ButtonStyle(
                        textButton = stringResource(R.string.AutoButton),
                        onClick = incrementHook.autoIncrement,
                        isPressed = incrementHook.autoButtonPressed,
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.spacerButtonStopZero),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ButtonStyle(
                        textButton = stringResource(R.string.StopButton),
                        onClick = incrementHook.stopIncrement,
                        isPressed = incrementHook.stopButtonPressed,
                        colors = ButtonDefaults.buttonColors(containerColor = Red)
                    )
                    ButtonStyle(
                        textButton = stringResource(R.string.ZeroButton),
                        onClick = incrementHook.zeroDisplay,
                        isPressed = incrementHook.zeroButtonPressed,
                        colors = ButtonDefaults.buttonColors(containerColor = Black)
                    )
                }
            }
        }
    }
}