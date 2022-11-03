package com.dramtar.billscollecting.presenter

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        lifecycleScope.launchWhenStarted {
            viewModel.updatingEvents.collectLatest { event ->
                when (event) {
                    is UIUpdatingEvent.OpenCreatedCSV -> {
                        startActivityWithCSVFile(event.file)
                    }
                }
            }
        }
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") { MainScreen(navController, viewModel = viewModel) }
                composable("overview") {
                    OverviewScreen(
                        navController,
                        viewModel = viewModel,
                        onExportCLicked = { exportDatabaseToCSVFile() })
                }
            }
        }

    }

    private fun exportDatabaseToCSVFile() {
        val csvFile = FileUtils.generateFile(this)
        csvFile?.let { viewModel.onUiEvent(UIEvent.ExportToCSV(it)) }
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
