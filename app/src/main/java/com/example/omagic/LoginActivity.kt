package com.example.omagic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener  {

    private val activity = this@LoginActivity

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btn_login: Button
    private lateinit var btnRegister: Button

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var inputValidation: InputValidation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // hiding the action bar
        supportActionBar!!.hide()

      /*  etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)*/

        // initializing the objects
        //initObjects()
        initViews()
        initListeners()
        btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


       /* btnLogin.setOnClickListener{
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)
            finish()
        }*/
        databaseHelper = DatabaseHelper(activity)
        inputValidation = InputValidation(activity)
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

        btn_login = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)

    }

    /**
     * This method is to initialize objects to be used
     *
     *    private fun initObjects() {
     * databaseHelper = DatabaseHelper(activity)
     * inputValidation = InputValidation(activity)
     *}
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> verifyFromSQLite()
        }
    }

     //This method is to validate the input text fields and verify login credentials from SQLite



    private fun initListeners() {
        btn_login!!.setOnClickListener(this)
    }



    private fun verifyFromSQLite() {
/*

        if (!inputValidation!!.isInputEditTextFilled(etEmail!!, et_email!!, getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(etEmail!!, et_email!!, getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(etPassword!!, et_password!!, getString(R.string.error_message_email))) {
            return
        }

        if (databaseHelper!!.checkUser(textInputEditTextEmail!!.text.toString().trim { it <= ' ' }, textInputEditTextPassword!!.text.toString().trim { it <= ' ' })) {
*/
            val accountsIntent = Intent(activity, UsersListActivity::class.java)
            accountsIntent.putExtra("EMAIL", etEmail!!.text.toString().trim { it <= ' ' })
            //emptyInputEditText()
            startActivity(accountsIntent)

        } /*else {

            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView!!, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show()
        }*/
    }


    /**
     * This method is to empty all input edit text

    private fun emptyInputEditText() {
        etEmail!!.text = null
        etPassword!!.text = null
    }
    */
