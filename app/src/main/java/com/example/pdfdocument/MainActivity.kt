package com.example.pdfdocument


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var imgView: ImageView
    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imgView = findViewById<ImageView>(R.id.img_pdf)

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            getPermission()
        }
    }


    fun getPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission.get(0)
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            permissionRequest.launch(permission)
        } else {
            selectPDF()
        }
    }

    val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.e("PermissionRequest", "" + JSONObject(it))

        }

    fun selectPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            setType("application/pdf")
        }
        pdfLauncher.launch(intent)
    }

    val pdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.e("pdfLauncher", "" + it.data)
        it.data?.data?.let { it1 -> convertPdfToImage(it1) }
    }


    fun convertPdfToImage(pdfUri: Uri) {
        Log.e("pdfLauncher", "convertPdfToImage------->" + pdfUri)
        try {
            val file: File = getFileFromUri(pdfUri)
            val fileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            // Open first page (index 0)
            val page = pdfRenderer.openPage(0)

            // Create a bitmap to render the PDF page
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Set bitmap to ImageView
            imgView.setImageBitmap(bitmap)

            // Close resources
            page.close()
            pdfRenderer.close()
            fileDescriptor.close()
        } catch (e: java.lang.Exception) {
            Log.e("pdfLauncher", "convertPdfToImage  Exception------->" + e.message)
            e.printStackTrace()
        }
    }


    @Throws(java.lang.Exception::class)
    fun getFileFromUri(uri: Uri): File {
        val file = File(cacheDir, getFileName(uri))
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while ((inputStream!!.read(buffer).also { length = it }) > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.close()
        inputStream.close()
        return file
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return if (result == null) "temp.pdf" else result!!
    }


}