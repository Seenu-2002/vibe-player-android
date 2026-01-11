package com.seenu.dev.android.vibeplayer.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonHover
import com.seenu.dev.android.vibeplayer.presentation.theme.textDisabled


@Composable
fun VibePlayerButton(
    iconRes: Int?,
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clip(CircleShape)
            .let {
                if (enabled) {
                    it.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                } else {
                    it.background(
                        color = MaterialTheme.colorScheme.buttonHover,
                        shape = CircleShape
                    )
                }
            }
            .clickable(enabled = enabled, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        val textColor = if (enabled) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.textDisabled
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = textColor
        )
    }
}

@Composable
fun VibePlayerPrimaryButton(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.buttonHover,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.textDisabled
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
        )
    }
}