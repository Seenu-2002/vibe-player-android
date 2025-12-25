package com.seenu.dev.android.vibeplayer.presentation.scan_music_config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.vibeplayer.presentation.design_system.ScanInputsCard
import com.seenu.dev.android.vibeplayer.presentation.design_system.VibePlayerNavIconButton
import com.seenu.dev.android.vibeplayer.presentation.shared_vm.ScanResultViewModel
import org.koin.compose.viewmodel.koinActivityViewModel
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanMusicScreen(onNavigateUp: () -> Unit, onScanCompleted: (Int) -> Unit) {

    val viewModel: ScanMusicViewModel = koinViewModel()
    val scanResultViewModel: ScanResultViewModel = koinActivityViewModel()
    val uiState by viewModel.scanMusicState.collectAsStateWithLifecycle()
    Timber.d("Shared view model instance: $scanResultViewModel")

    LaunchedEffect(Unit) {
        viewModel.scanResultState.collect { scanResultState ->
            scanResultViewModel.publishResult(scanResultState.scannedSongsCount)
            onScanCompleted(scanResultState.scannedSongsCount)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, navigationIcon = {
            VibePlayerNavIconButton(onClick = onNavigateUp)
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScanInputsCard(
                modifier = Modifier.fillMaxWidth(),
                selectedSize = uiState.scanConfig.minSize,
                selectedDuration = uiState.scanConfig.minDuration,
                isScanning = uiState.scanState == ScanState.Scanning,
                onSizeSelected = { newSize ->
                    viewModel.onIntent(ScanMusicIntent.UpdateSize(newSize))
                },
                onDurationSelected = { newDuration ->
                    viewModel.onIntent(ScanMusicIntent.UpdateDuration(newDuration))
                },
                onScan = {
                    viewModel.onIntent(ScanMusicIntent.Scan)
                }
            )
        }
    }
}