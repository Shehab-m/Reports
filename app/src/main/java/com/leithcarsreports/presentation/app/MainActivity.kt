package com.leithcarsreports.presentation.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
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
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val openDocumentLauncher =
//            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
//                uri?.let { documentUri ->
//                    try {
//                        val inputStream: InputStream? = contentResolver.openInputStream(documentUri)
//                        if (inputStream != null) {
//                            // Successfully opened the InputStream, proceed with reading Excel file
//                            readExcelFile(inputStream)
//                        } else {
//                            // InputStream is null, handle the error
//                            Log.e(
//                                "registerForActivityResult",
//                                "Failed to open InputStream for document: $documentUri"
//                            )
//                            // Display an error message to the user or handle the situation accordingly
//                        }
//                    } catch (e: IOException) {
//                        // An error occurred while opening the InputStream
//                        Log.e("registerForActivityResult", "Error reading Excel file", e)
//                        // Display an error message to the user or handle the situation accordingly
//                    }
//                }
//            }
//        fun openFilePicker() {
//            val mimeTypes = arrayOf("application/vnd.ms-excel") // Specify MIME types to filter Excel files if needed
//            openDocumentLauncher.launch(mimeTypes)
//        }
        setContent {
            CarsReportsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReportsScreen()
                }
            }
        }
    }
}



