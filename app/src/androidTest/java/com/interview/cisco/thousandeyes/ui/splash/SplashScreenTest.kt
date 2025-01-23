package com.interview.cisco.thousandeyes.ui.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.interview.cisco.thousandeyes.ui.theme.ThousandEyesAppTheme
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun splashScreenTest() {
        // Start the app
        composeTestRule.setContent {
            ThousandEyesAppTheme {
                SplashScreen{}
            }
        }

        composeTestRule.onNodeWithContentDescription("Splash Screen").assertIsDisplayed()
    }
}