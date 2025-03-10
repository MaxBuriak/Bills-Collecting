package com.dramtar.billscollecting.presenter

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.presenter.bills.MainScreen
import com.dramtar.billscollecting.presenter.overview.OverviewScreen
import com.dramtar.billscollecting.presenter.overview.OverviewState
import com.dramtar.billscollecting.presenter.type_overview.TypeOverviewScreen
import com.dramtar.billscollecting.utils.FileUtils
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Screen.BillsScreen.route) {
                composable(route = Screen.BillsScreen.route) {
                    MainScreen(navController, billAdded = { playAddBillSound() })
                }
                composable(
                    route = Screen.OverviewScreen.route
                            + "?minDate={minDate}&maxDate={maxDate}",
                    arguments = listOf(
                        navArgument(
                            name = "minDate"
                        ) {
                            type = NavType.LongType
                            defaultValue = -1L
                        },
                        navArgument(
                            name = "maxDate"
                        ) {
                            type = NavType.LongType
                            defaultValue = -1L
                        }
                    )
                ) {
                    OverviewScreen(
                        navController,
                        onExportCLicked = { overviewState ->
                            exportBillsOverviewToCSVFile(overviewState)
                        })
                }
                composable(
                    route = Screen.TypeOverviewScreen.route + "?typeId={typeId}",
                    arguments = listOf(navArgument(name = "typeId") {
                        type = NavType.StringType
                        defaultValue = ""
                    })
                ) {
                    TypeOverviewScreen(navController = navController, modifier = Modifier)
                }
            }
        }
    }

    private fun exportBillsOverviewToCSVFile(overviewState: OverviewState) {
        val csvFile = FileUtils.generateFile(
            context = this,
            fileName = "${overviewState.fmtPeriodOfTime} Bills overview.csv"
        )
        csvFile?.let {
            lifecycleScope.launch { //TODO CHECK THIS
                val overviewBillsList = overviewState.gropedByTypesBills
                val formattedDate = overviewState.fmtPeriodOfTime
                csvWriter().open(csvFile, append = false) {
                    writeRow(listOf("", "Overview for $formattedDate"))
                    writeRow(listOf("Type", "Amount of payments", "Percentage"))
                    overviewBillsList?.forEach { bill ->
                        writeRow(
                            listOf(
                                bill.type.name,
                                bill.formattedSumAmount,
                                bill.formattedPercentage
                            )
                        )
                    }
                    writeRow(listOf("Total sum", overviewState.fmtTotalSum))
                }
                startActivityWithCSVFile(csvFile)
            }
        }
    }

    private fun playAddBillSound() {
        MediaPlayer.create(this, R.raw.type_in).start()
    }

    private fun startActivityWithCSVFile(file: File) {
        val intent = FileUtils.goToFileIntent(this, file)
        startActivity(intent)
    }
}
