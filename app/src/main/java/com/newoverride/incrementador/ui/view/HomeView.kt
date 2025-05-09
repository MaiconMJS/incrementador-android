package com.newoverride.incrementador.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.newoverride.incrementador.R
import com.newoverride.incrementador.dimens.Dimens
import com.newoverride.incrementador.enum.ButtonType
import com.newoverride.incrementador.enum.NumberType
import com.newoverride.incrementador.hooks.incrementHook
import com.newoverride.incrementador.ui.components.ButtonStyle
import com.newoverride.incrementador.ui.components.Display
import com.newoverride.incrementador.ui.theme.BlackLow
import com.newoverride.incrementador.ui.theme.Blue
import com.newoverride.incrementador.ui.theme.Green
import com.newoverride.incrementador.ui.theme.IncrementadorTheme
import com.newoverride.incrementador.ui.theme.Red

@Composable
fun HomeView() {

    val incrementHook = incrementHook()

    IncrementadorTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Display(
                        incrementHook.number[NumberType.N1] ?: 0,
                        incrementHook.number[NumberType.N2] ?: 0
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ButtonStyle(
                            textButton = stringResource(R.string.ManualButton),
                            onClick = incrementHook.manualIncrement,
                            isPressed = incrementHook.buttonPressedState[ButtonType.MANUAL] == true,
                            colors = ButtonDefaults.buttonColors(containerColor = Blue)
                        )
                        ButtonStyle(
                            textButton = stringResource(R.string.AutoButton),
                            onClick = incrementHook.autoIncrement,
                            isPressed = incrementHook.buttonPressedState[ButtonType.AUTO] == true,
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
                            isPressed = incrementHook.buttonPressedState[ButtonType.STOP] == true,
                            colors = ButtonDefaults.buttonColors(containerColor = Red)
                        )
                        ButtonStyle(
                            textButton = stringResource(R.string.ZeroButton),
                            onClick = incrementHook.zeroDisplay,
                            isPressed = incrementHook.buttonPressedState[ButtonType.ZERO] == true,
                            colors = ButtonDefaults.buttonColors(containerColor = BlackLow)
                        )
                    }
                }
            }
        }
    }
}