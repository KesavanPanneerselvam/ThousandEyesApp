package com.interview.cisco.thousandeyes.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowSpaceExtraSmall() = Spacer(
    modifier = Modifier
        .width(4.dp)
        .background(Color.Transparent)
        .height(8.dp)
)

@Composable
fun RowSpaceSmall() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(8.dp)
)

@Composable
fun RowSpaceMedium() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(16.dp)
)

@Composable
fun RowSpaceLarge() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(32.dp)
)

@Composable
fun ColumnSpaceExtraSmall() = Spacer(
    modifier = Modifier
        .width(4.dp)
        .background(Color.Transparent)
        .height(4.dp)
)
@Composable
fun ColumnSpaceSmall() = Spacer(
    modifier = Modifier
        .width(8.dp)
        .background(Color.Transparent)
        .height(8.dp)
)

@Composable
fun ColumnSpaceMedium() = Spacer(
    modifier = Modifier
        .width(16.dp)
        .background(Color.Transparent)
        .height(16.dp)
)

@Composable
fun ColumnSpaceLarge() = Spacer(
    modifier = Modifier
        .height(32.dp)
        .background(Color.Transparent)
        .width(8.dp)
)

