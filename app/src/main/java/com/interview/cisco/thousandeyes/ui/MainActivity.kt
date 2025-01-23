package com.interview.cisco.thousandeyes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.interview.cisco.thousandeyes.navigation.NavHost
import com.interview.cisco.thousandeyes.ui.theme.ThousandEyesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThousandEyesAppTheme {
                NavHost()
            }
        }
    }
}