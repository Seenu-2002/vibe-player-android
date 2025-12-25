package com.seenu.dev.android.vibeplayer.presentation.design_system

import android.R.attr.label
import android.R.attr.onClick
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.vibeplayer.R
import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.presentation.theme.VibePlayerTheme
import com.seenu.dev.android.vibeplayer.presentation.theme.bodyLargeMedium
import com.seenu.dev.android.vibeplayer.presentation.theme.buttonPrimary30
import com.seenu.dev.android.vibeplayer.presentation.theme.textDisabled

@Preview
@Composable
private fun ScanInputsCardPreview() {
    VibePlayerTheme {
        var selectedDuration by remember {
            mutableStateOf(ScanConfig.MinDuration.SEC_30)
        }
        var selectedSize by remember {
            mutableStateOf(ScanConfig.MinSize.KB_100)
        }
        ScanInputsCard(
            selectedDuration = selectedDuration,
            selectedSize = selectedSize,
            onDurationSelected = {
                selectedDuration = it
            },
            onSizeSelected = {
                selectedSize = it
            },
            isScanning = false,
            onScan = {},
        )
    }
}

@Composable
fun ScanInputsCard(
    selectedDuration: ScanConfig.MinDuration,
    selectedSize: ScanConfig.MinSize,
    onDurationSelected: (ScanConfig.MinDuration) -> Unit,
    onSizeSelected: (ScanConfig.MinSize) -> Unit,
    isScanning: Boolean,
    onScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScanningAnimation(
            animate = isScanning,
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.ignore_songs_less_than_duration_msg),
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            VibePlayerRadioButton(
                selectedDuration == ScanConfig.MinDuration.SEC_30,
                label = "30s",
                onClick = {
                    onDurationSelected(ScanConfig.MinDuration.SEC_30)
                },
                modifier = Modifier.weight(1F)
            )
            Spacer(modifier = Modifier.width(8.dp))
            VibePlayerRadioButton(
                selectedDuration == ScanConfig.MinDuration.SEC_60,
                label = "60s",
                onClick = {
                    onDurationSelected(ScanConfig.MinDuration.SEC_60)
                },
                modifier = Modifier.weight(1F)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.ignore_songs_less_than_size_msg),
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            VibePlayerRadioButton(
                selected = selectedSize == ScanConfig.MinSize.KB_100,
                label = "100KB",
                onClick = {
                    onSizeSelected(ScanConfig.MinSize.KB_100)
                },
                modifier = Modifier.weight(1F)
            )
            Spacer(modifier = Modifier.width(8.dp))
            VibePlayerRadioButton(
                selected = selectedSize == ScanConfig.MinSize.KB_500,
                label = "500KB",
                onClick = {
                    onSizeSelected(ScanConfig.MinSize.KB_500)
                },
                modifier = Modifier.weight(1F)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            enabled = !isScanning,
            onClick = onScan,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.textDisabled
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.scanning),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.textDisabled
                )
            } else {
                Text(
                    text = stringResource(R.string.scan),
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun VibePlayerRadioButton(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strokeColor = if (selected) {
        MaterialTheme.colorScheme.buttonPrimary30
    } else {
        MaterialTheme.colorScheme.outline
    }
    Row(
        modifier = modifier
            .border(
                1.dp,
                strokeColor,
                CircleShape
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSecondary
            )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.weight(1F)
        )
    }
}