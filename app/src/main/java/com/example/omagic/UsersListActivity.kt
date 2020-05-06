package com.example.omagic


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_users_list.*


class UsersListActivity : AppCompatActivity() {

    private val activity = this@UsersListActivity
    private lateinit var textViewName: AppCompatTextView
    private lateinit var recyclerViewEvent: RecyclerView
    private lateinit var listUsers: MutableList<User>
    private lateinit var listEvent: MutableList<Event>
    private lateinit var eventRecyclerAdapter: EventRecyclerAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)
        //supportActionBar!!.title = ""
        initViews()
        initObjects()

        button4.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
            finish()
        }
    }
    /**
     * This method is to initialize views
     */
    private fun initViews() {
        textViewName = findViewById<View>(R.id.textViewName) as AppCompatTextView
        recyclerViewEvent = findViewById<View>(R.id.recyclerViewEvent) as RecyclerView
    }

    /**
     * This method is to initialize objects to be used
     */
    private fun initObjects() {
        listEvent = ArrayList()
        eventRecyclerAdapter = EventRecyclerAdapter(listEvent)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewEvent.layoutManager = mLayoutManager
        recyclerViewEvent.itemAnimator = DefaultItemAnimator()
        recyclerViewEvent.setHasFixedSize(true)
        recyclerViewEvent.adapter = eventRecyclerAdapter
        databaseHelper = DatabaseHelper(activity)

        val emailFromIntent = intent.getStringExtra("EMAIL")
        textViewName.text = emailFromIntent

        var getDataFromSQLite = GetDataFromSQLite()
        getDataFromSQLite.execute()
    }

    /**
     * This class is to fetch all user records from SQLite
     */
    inner class GetDataFromSQLite : AsyncTask<Void, Void, List<Event>>() {

        override fun doInBackground(vararg p0: Void?): List<Event> {
            return databaseHelper.getAllEvent()
        }

        override fun onPostExecute(result: List<Event>?) {
            super.onPostExecute(result)
            listEvent.clear()
            listEvent.addAll(result!!)
        }

    }

}