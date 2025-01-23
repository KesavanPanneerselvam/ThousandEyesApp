package com.interview.cisco.thousandeyes.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.interview.cisco.thousandeyes.ui.theme.screenBackground
import com.interview.cisco.thousandeyes.utils.getColors


@Composable
fun BaseScreen(
    viewModel: BaseViewModel,
    isScrollable: Boolean = false,
    content: @Composable() () -> Unit
) {
    Box(
        modifier = Modifier.background(color = screenBackground()),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val screenModifier = if(isScrollable) {
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .matchParentSize()
            }
            else{
                Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .matchParentSize()
            }
            Column(
                modifier = screenModifier
            ) {
                content.invoke()
            }
            viewModel.apply {
                ProgressDialog(showDialog = showLoading)
                showSuccessMessage.value.let {
                    if (it.enable) {
                        SnackBarLayout(it.message, getColors().primary, Color.White)
                    }
                }
                showErrorMessage.value.let {
                    if (it.enable) {
                        SnackBarLayout(it.message, getColors().error, getColors().errorContainer)
                    }
                }
            }
        }
    }
}

@Composable
fun CardLayout(fullHeight: Boolean = false,isScrollable: Boolean = false, content: @Composable() () -> Unit) {
    if(fullHeight) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxHeight(1f)
                        .fillMaxWidth()
                        .onSizeChanged { size = it }
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getColors().background,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        var modifier = Modifier
                            .padding(8.dp)
                            .fillMaxHeight()
                        if(isScrollable){
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                        }
                        Column(modifier = modifier) {
                            content.invoke()
                        }
                    }
                }
            }
        }
    }else {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = getColors().background,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight()) {
                content.invoke()
            }
        }
    }
}