package com.dramtar.billscollecting.presenter

sealed class Screen(val route: String) {
    object BillsScreen: Screen("bills_screen")
    object OverviewScreen: Screen("overview_screen")
    object TypeOverviewScreen: Screen("type_overview_screen")
}
