package com.example.omagic

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class CreateEvent : AppCompatActivity(), View.OnClickListener {

    private val activity = this@CreateEvent

    private lateinit var  idName: EditText
    private lateinit var idLocation: EditText
    private lateinit var idDescription: EditText
    private lateinit var btn_submit: Button
    private lateinit var btn_upload: Button
    private lateinit var imageView: ImageView
    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper
    var CameraRequestCode :Int = 200
    var GalleryRequestCode :Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        // hiding the action bar
        supportActionBar!!.hide()

        initViews()
        initListeners()
        initObjects()

        imageView.setOnClickListener {
            var dialogueBox = AlertDialog.Builder(this)
            var dialogueOptions = arrayOf("Camera","Gallery")
            dialogueBox.setTitle("Make Choice")
            dialogueBox.setItems(dialogueOptions,object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if(dialogueOptions[p1].equals("Camera")) {
                        OpenCamera()
                    }
                    if (dialogueOptions[p1].equals("Gallery")) {
                        OpenGallery()
                    }
                }
            })
            dialogueBox.show()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_submit -> postDataToSQLite()

        }
    }
    private fun initViews() {

        idName = findViewById<View>(R.id.id_name) as EditText
        idLocation = findViewById<View>(R.id.id_location) as EditText
        idDescription = findViewById<View>(R.id.id_description) as EditText
       // btn_upload = findViewById<View>(R.id.btn_upload) as Button
        btn_submit = findViewById<View>(R.id.btn_submit) as Button
        imageView = findViewById<View>(R.id.imageView)!! as ImageView
       // id_name = findViewById<EditText>(R.id.text)

    }
    private fun initListeners() {
        btn_submit.setOnClickListener(this)
        //appCompatTextViewLoginLink!!.setOnClickListener(this)
    }

    private fun initObjects() {
        inputValidation = InputValidation(activity)
        databaseHelper = DatabaseHelper(activity)
    }


    private fun postDataToSQLite() {
            val event = Event(name = idName!!.text.toString().trim(),location = idLocation!!.text.toString().trim(), description = idDescription!!.text.toString().trim(), KEY_IMAGE = imageViewToByte(imageView))
            databaseHelper!!.addEvent(event)
            // Snack Bar to show success message that record saved successfully
            // Snackbar.make(nestedScrollView!!, getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
        val intent = Intent(this, UsersListActivity::class.java)
        startActivity(intent)
        finish()
            emptyInputEditText()
        }

    private fun emptyInputEditText() {
        idName!!.text = null
        idDescription!!.text = null
        //textInputEditTextPassword!!.text = null
        //textInputEditTextConfirmPassword!!.text = null
    }

 /*   private fun OpenGallery() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            var GalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GalleryIntent,GalleryRequestCode)
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),GalleryRequestCode)
        }
    }*/


    private fun OpenGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       // val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, GalleryRequestCode)
    }

    private fun OpenCamera() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
            val CameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(CameraIntent,CameraRequestCode)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),CameraRequestCode)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            CameraRequestCode-> {
            if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                OpenCamera()
            } else {
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
            }
            }
            GalleryRequestCode-> {
            if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                OpenGallery()
            } else {
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }


    fun imageViewToByte(image: ImageView): ByteArray {
        val bitmap =(image.drawable as BitmapDrawable)?.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GalleryRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri = data.data!!
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
