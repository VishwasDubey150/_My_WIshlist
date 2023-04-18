package com.example.mywishlist.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mywishlist.R
import com.example.mywishlist.database.DatabaseHandler
import com.example.mywishlist.model.MyWishlistModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class addlist : AppCompatActivity() {
    companion object {
        private const val GALLERY = 1
        private const val IMAGE_DIRECTORY = "MyWishlist"
        private var saveImageToInternalStorage:Uri?=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addlist)
        supportActionBar?.hide()
    }
    fun back(view: View) {
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun save(view: View) {


        var Name=findViewById<TextView>(R.id.name)
        var dates=findViewById<TextView>(R.id.dates)
        var description=findViewById<TextView>(R.id.description)
        var pic=findViewById<ImageView>(R.id.pic)

        when{
            Name.text.isNullOrEmpty() ->{
                Toast.makeText(this,"Enter you name",Toast.LENGTH_SHORT).show()
            }
            dates.text.isNullOrEmpty() ->{
                Toast.makeText(this,"Enter expected date",Toast.LENGTH_SHORT).show()
            }
            description.text.isNullOrEmpty() ->{
                Toast.makeText(this,"Enter short description about place",Toast.LENGTH_SHORT).show()
            }
            Name.text.isNullOrEmpty() ->{
                Toast.makeText(this,"Enter you name",Toast.LENGTH_SHORT).show()
            }
            saveImageToInternalStorage == null ->{
                Toast.makeText(this,"Please select an image",Toast.LENGTH_LONG).show()
            }
            else ->{
                val MyWishlistModel=MyWishlistModel(
                    0,Name.text.toString(),pic.toString(),description.text.toString(),dates.text.toString())

                val  dbHandler=DatabaseHandler(this)
                val addMyWishlist=dbHandler.addWishlist(MyWishlistModel)

                if(addMyWishlist>0)
                {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    fun date(view: View) {
        var dates=findViewById<TextView>(R.id.dates)
        val myCalandar = Calendar.getInstance()
        val year=myCalandar.get(Calendar.YEAR)
        val month=myCalandar.get(Calendar.MONTH)
        val day = myCalandar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                view, Syear, Smonth, Sdayofmonth ->
            val selected="$Sdayofmonth/${Smonth+1}/${Syear}"
            dates.setText(selected)
        }
            ,year
            ,month
            ,day).show()
    }

    fun addimage(view: View) {
        choosePhotofromGalary()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dp = findViewById<ImageView>(R.id.pic)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        dp!!.setImageBitmap(selectedImageBitmap)
                        saveImageToInternalStorage=saveImageToInternalStorage(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Permission denied",  Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun choosePhotofromGalary() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?)
            {
                showRDforpermissions()
            }
        }).onSameThread().check()

    }

    private fun showRDforpermissions() {
        AlertDialog.Builder(this)
            .setMessage("its look like you have turned off permission required for this feature.It can be enabled under the Application Settings")
            .setPositiveButton("Allow from setting")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("Package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }
}