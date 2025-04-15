package com.newoverride.incrementador.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.newoverride.incrementador.dimens.Dimens

@Composable
fun Display(n1: Int, n2: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.spacerDisplayButton),
    ) {
        Text(text = "N1: $n1", style = MaterialTheme.typography.titleLarge)
        Text(text = "N2: $n2", style = MaterialTheme.typography.titleLarge)
    }
}