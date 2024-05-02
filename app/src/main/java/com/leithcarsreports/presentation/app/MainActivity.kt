package com.leithcarsreports.presentation.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.leithcarsreports.presentation.composable.PermissionDialog
import com.leithcarsreports.presentation.reports.ReportsScreen
import com.leithcarsreports.presentation.ui.theme.CarsReportsTheme
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarsReportsTheme {
                CheckPermissions()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReportsScreen()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun CheckPermissions() {
        val permissions = listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
        )
        val isDialogOpen = remember { mutableStateOf(true) }
        val permissionState = rememberMultiplePermissionsState(permissions)
        when {
            !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                LaunchedEffect(Unit) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
            permissionState.shouldShowRationale && isDialogOpen.value -> {
                PermissionDialog(
                    onDismissRequest = { isDialogOpen.value = false },
                    message = "Permission Required",
                    onClickDismiss = { isDialogOpen.value = false },
                    onClickGoToSettings = {
                        isDialogOpen.value = false
                        openAppSettings()
                    }
                )
            }
        }
    }



    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}



