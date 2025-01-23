package com.interview.cisco.thousandeyes.core

import AppTypography
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.interview.cisco.thousandeyes.utils.getColors

@Composable
fun LabelHeading(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    color: Color = getColors().onBackground,
    modifier: Modifier = Modifier
) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier.padding(bottom = 4.dp),
    color = color,
    fontWeight = FontWeight.Bold,
    textAlign = textAlign
)

@Composable
fun LabelContent(
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    color: Color = getColors().onBackground,
    modifier: Modifier = Modifier
) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color,
    fontWeight = FontWeight.Normal,
    textAlign = textAlign
)