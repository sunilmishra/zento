package com.codewithmisu.zento.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 1.5.sp
            )
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text.uppercase(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TertiaryButton(
    text: String,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onLoginClick) {
        Text(
            text,
            modifier = modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium.copy(
                textDecoration = TextDecoration.Underline,
                letterSpacing = 1.5.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}


