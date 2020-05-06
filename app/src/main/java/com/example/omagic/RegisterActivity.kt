package com.example.omagic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterActivity

    private lateinit var  etEmail: EditText
    private lateinit var  etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // hiding the action bar
        supportActionBar!!.hide()
        // initializing the views
        initViews()
        // initializing the listeners
        initListeners()
        // initializing the objects
        initObjects()
    }

    private fun initViews() {
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etName = findViewById<View>(R.id.et_name) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnRegister = findViewById<View>(R.id.btn_register) as Button
    }

    private fun initListeners() {
        btnRegister.setOnClickListener(this)
        //appCompatTextViewLoginLink!!.setOnClickListener(this)
    }

    private fun initObjects() {
        inputValidation = InputValidation(activity)
        databaseHelper = DatabaseHelper(activity)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btn_register -> postDataToSQLite()

           // R.id.appCompatTextViewLoginLink -> finish()
        }
    }
    private fun postDataToSQLite() =
        if (!databaseHelper!!.checkUser(etEmail!!.text.toString().trim())) {

            val user = User(name = etName!!.text.toString().trim(),email = etEmail!!.text.toString().trim(), password = etPassword!!.text.toString().trim())

            databaseHelper!!.addUser(user)

            // Snack Bar to show success message that record saved successfully
           // Snackbar.make(nestedScrollView!!, getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)
            finish()
            emptyInputEditText()

        } else {
            // Snack Bar to show error message that record already exists
           // Snackbar.make(nestedScrollView!!, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
        }
    private fun emptyInputEditText() {
        etEmail!!.text = null
        etPassword!!.text = null
        //textInputEditTextPassword!!.text = null
        //textInputEditTextConfirmPassword!!.text = null
    }
}
