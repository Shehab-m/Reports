//package com.leithcarsreports.data.services
//
//import android.content.Context
//import com.leithcarsreports.presentation.app.MainActivity
//import javax.inject.Inject
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Build
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat
//import org.apache.poi.ss.usermodel.Cell
//import org.apache.poi.ss.usermodel.Row
//import org.apache.poi.ss.usermodel.WorkbookFactory
//import java.io.IOException
//import java.io.InputStream
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import kotlin.coroutines.resume
//import kotlin.coroutines.resumeWithException
//import kotlin.coroutines.suspendCoroutine
//
//class FilePickerManager @Inject constructor(
//    private val context: Context,
////    private val activity: MainActivity
//) : IFilePickerManager {
//
//    private var activity: MainActivity? = null
//
//    // Method to set the activity instance
//    fun setActivity(activity: MainActivity) {
//        this.activity = activity
//    }
//
//
//    private val openDocumentLauncher =
//        activity?.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
//            uri?.let { documentUri ->
//                try {
//                    val inputStream: InputStream? =
//                        activity?.contentResolver?.openInputStream(documentUri)
//                    if (inputStream != null) {
//                        // Successfully opened the InputStream, proceed with reading Excel file
//                        readExcelFile(inputStream)
//                    } else {
//                        // InputStream is null, handle the error
//                        Log.e(
//                            "registerForActivityResult",
//                            "Failed to open InputStream for document: $documentUri"
//                        )
//                        // Display an error message to the user or handle the situation accordingly
//                    }
//                } catch (e: IOException) {
//                    // An error occurred while opening the InputStream
//                    Log.e("registerForActivityResult", "Error reading Excel file", e)
//                    // Display an error message to the user or handle the situation accordingly
//                }
//            }
//        }
//
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun pickExcelFile() {
//        Log.d("pickExcelFile", "pickExcelFile: ")
//        if (hasStoragePermission()) {
//            Log.d("pickExcelFile", "true")
//            openFilePicker()
//        } else {
//            Log.d("pickExcelFile", "false")
//            requestStoragePermission()
//        }
//    }
//
//    private fun openFilePicker() {
//        val mimeTypes =
//            arrayOf("application/vnd.ms-excel") // Specify MIME types to filter Excel files if needed
//        Log.d("openFilePicker: ", "${openDocumentLauncher}")
//        openDocumentLauncher?.launch(mimeTypes)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private fun hasStoragePermission(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_MEDIA_IMAGES
//        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_MEDIA_VIDEO
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private val requestPermissionLauncher =
//        activity?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission is granted, proceed with file selection
//                openFilePicker()
//            } else {
//                // Permission is denied, handle accordingly
//            }
//        }
//
//    private fun requestStoragePermission() {
//        requestPermissionLauncher?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//    }
//
//    private fun readExcelFile(inputStream: InputStream): String {
//        return try {
//            // Use Apache POI or any other library to read the Excel file from the inputStream
//            // Example:
//            val workbook = WorkbookFactory.create(inputStream)
//            val sheet = workbook.getSheetAt(0) // Assuming your data is on the first sheet
//            val rowIndex = 0 // Row index of the data you want to access
//            val colIndex = 1 // Column index of the data you want to access
//            val row: Row = sheet.getRow(rowIndex)
//            val cell: Cell = row.getCell(colIndex)
//            val value: String = cell.toString() // Assuming the data is a string, adjust accordingly
//            return value
//            // Proceed with parsing and processing the Excel data
//        } catch (e: Exception) {
//            // An error occurred while reading the Excel file
//            Log.e("readExcelFile", "Error parsing Excel file", e)
//            // Display an error message to the user or handle the situation accordingly
//            "error"
//        } finally {
//            try {
//                inputStream.close() // Close the InputStream when done
//            } catch (e: IOException) {
//                // Error occurred while closing the InputStream
//                Log.e("readExcelFile", "Error closing InputStream", e)
//                // Handle the situation accordingly, although this is unlikely to occur
//            }
//        }
//    }
//}