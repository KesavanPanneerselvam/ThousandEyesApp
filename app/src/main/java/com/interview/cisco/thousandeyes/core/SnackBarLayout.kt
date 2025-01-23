package com.interview.cisco.thousandeyes.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SnackBarLayout(
    message: String,
    backgroundColor: Color,
    textColor: Color
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
    }

    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(
            modifier = Modifier.padding(bottom = 16.dp),
            snackbarData = it,
            containerColor = backgroundColor,
            contentColor = textColor
        )
    }
}