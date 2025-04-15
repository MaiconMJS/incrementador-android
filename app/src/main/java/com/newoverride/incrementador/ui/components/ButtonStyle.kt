package com.newoverride.incrementador.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.newoverride.incrementador.dimens.Dimens

@Composable
fun ButtonStyle(
    textButton: String,
    onClick: () -> Unit,
    colors: ButtonColors,
    isPressed: Boolean
) {
    val scale = animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "spring-press-scale"
    )

    Button(
        onClick = onClick,
        colors = colors,
        modifier = Modifier
            .scale(scale.value)
            .height(Dimens.heightButton)
            .width(Dimens.widthButton)
    ) {
        Text(text = textButton, style = MaterialTheme.typography.labelSmall)
    }
}