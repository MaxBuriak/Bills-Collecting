package com.dramtar.billscollecting.presenter

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dramtar.billscollecting.R
import com.dramtar.billscollecting.ui.theme.BillsCollectingTheme
import com.dramtar.billscollecting.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") { MainScreen(navController, viewModel = viewModel) }
                composable("overview") {
                    OverviewScreen(
                        navController,
                        overviewData = viewModel.billListState.overviewData,
                        onExportCLicked = { exportDatabaseToCSVFile() },
                        onTestClick = { playAddBillSound() })
                }
                composable("type_overview") {
                    TypeOverviewScreen(
                        navController = navController,
                        modifier = Modifier,
                        typeOverviewData = viewModel.billListState.typeOverviewData)
                }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.updatingEvents.collectLatest { event ->
                    when (event) {
                        is UIUpdatingEvent.OpenCreatedCSV -> {
                            startActivityWithCSVFile(event.file)
                        }
                        is UIUpdatingEvent.AddBillTypeClicked -> playAddBillSound()
                        is UIUpdatingEvent.NavigateToOverview -> navController.navigate("overview")
                        is UIUpdatingEvent.NavigateToTypeOverview -> navController.navigate("type_overview")
                    }
                }
            }
        }

    }

    private fun exportDatabaseToCSVFile() {
        val csvFile = FileUtils.generateFile(context = this, fileName = viewModel.getCSVFileName())
        csvFile?.let { viewModel.onUiEvent(UIEvent.ExportToCSV(it)) }
    }

    private fun playAddBillSound() {
        MediaPlayer.create(this, R.raw.type_in).start()
    }

    private fun startActivityWithCSVFile(file: File) {
        val intent = FileUtils.goToFileIntent(this, file)
        //if (this.packageManager.resolveActivity(intent, 0) == null) {
        startActivity(intent)
        /*    Toast.makeText(
                this,
                "File created and start opening",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                "Something goes wrong",
                Toast.LENGTH_LONG
            ).show()
        }*/
    }
}

@Preview
@Composable
fun MainPreview() {
    BillsCollectingTheme {}
}
