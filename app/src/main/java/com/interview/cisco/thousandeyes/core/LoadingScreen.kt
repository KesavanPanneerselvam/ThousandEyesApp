package com.interview.cisco.thousandeyes.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.interview.cisco.thousandeyes.utils.DotsFlashing

@Composable
fun ProgressDialog(showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                DotsFlashing()
            }
        }
    }
}