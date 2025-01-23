package com.interview.cisco.thousandeyes.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.interview.cisco.thousandeyes.R
import com.interview.cisco.thousandeyes.core.BaseScreen
import com.interview.cisco.thousandeyes.core.CardLayout
import com.interview.cisco.thousandeyes.core.ColumnSpaceMedium
import com.interview.cisco.thousandeyes.core.ColumnSpaceSmall
import com.interview.cisco.thousandeyes.core.LabelContent
import com.interview.cisco.thousandeyes.core.LabelHeading
import com.interview.cisco.thousandeyes.domain.model.HostItem
import com.interview.cisco.thousandeyes.ui.theme.ThousandEyesAppTheme
import com.interview.cisco.thousandeyes.utils.UIState
import com.interview.cisco.thousandeyes.utils.getColors

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getHostList()
    }
    BaseScreen(viewModel = viewModel) {
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    LabelHeading("Sort By Name:", modifier = Modifier.padding(top = 8.dp))
                    Checkbox(
                        checked = viewModel.sortByName.value,
                        onCheckedChange = {
                            viewModel.sortByName.value = it
                        }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {
                when (val result = viewModel.hostItems.collectAsState().value) {
                    is UIState.Success -> {
                        val hostList = if (viewModel.sortByName.value) {
                            result.data.sortedBy { it.name }
                        } else {
                            result.data
                        }
                        hostList.forEachIndexed { index, hostItem ->
                            HostView(hostItem) {
                                viewModel.getHostDetails(index, hostList.toMutableList())
                            }
                            ColumnSpaceMedium()
                        }
                    }

                    else -> {}
                }

            }
            Column(modifier = Modifier.padding(top = 16.dp)) {
                ButtonNormal("Check All Hosts") {
                    viewModel.getHostList()
                }
            }
        }
    }
}

@Composable
fun HostView(hostItem: HostItem, onClick: () -> Unit) {
    CardLayout {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NetworkImage(
                imageUrl = hostItem.icon,
                contentDescription = hostItem.name,
                modifier = Modifier
                    .height(90.dp)
            )
            LabelHeading(hostItem.name)
            LabelContent(hostItem.url)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    InstanceView("Total", hostItem.total.toString())
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    InstanceView("Success", hostItem.success.toString())
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    InstanceView("Failure", hostItem.failure.toString())
                }
            }
            ColumnSpaceSmall()
            LabelHeading("Average Latency: ${hostItem.latency?.let { "$it ms" } ?: "The target host is down or inaccessible"}")
            ColumnSpaceSmall()
        }
        ButtonNormal("Check Now") {
            onClick.invoke()
        }
        ColumnSpaceSmall()
    }
}

@Composable
fun InstanceView(title: String, count: String) {
    CardLayout {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            LabelHeading(title, textAlign = TextAlign.Center)
            LabelContent(count)
        }
    }
}

@Composable
fun ButtonNormal(
    text: String,
    onClick: () -> Unit = { },
) {
    Button(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
    ) {
        Box(
            modifier = Modifier
                .background(getColors().primary)
                .then(Modifier)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            LabelHeading(text = text, color = getColors().background)
        }
    }
}

@Composable
fun NetworkImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .error(R.drawable.ic_image_not_available) // Add your error drawable here
            .placeholder(R.drawable.ic_image_loading) // Optional loading placeholder
            .build()
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HostViewPreview() {
    ThousandEyesAppTheme {
        HostView(
            HostItem(
                name = "eBay",
                url = "www.ebay.co.uk",
                icon = "https://pages.ebay.com/favicon.ico",
                latency = 11
            )
        ) {}
    }
}