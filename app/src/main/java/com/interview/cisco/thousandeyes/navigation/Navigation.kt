package com.interview.cisco.thousandeyes.navigation

sealed class Navigation(val destination: String) {
    data object Splash : Navigation("splash")
    data object Home : Navigation("home")
}

sealed class Route(val value: String) {
    data object Main : Navigation("main")
}