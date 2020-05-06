package com.example.omagic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        // Database Version
        private val DATABASE_VERSION = 1
        // Database Name
        private val DATABASE_NAME = "OMagic.db"
        // User table name
        private val TABLE_USER = "user"
        // User Table Columns names
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"

// all events goes here
        private val TABLE_EVENT = "event"
        private val COLUMN_EVENT_ID = "event_id"
        private val COLUMN_EVENT_NAME = "event_name"
        private val COLUMN_EVENT_LOCATION = "event_location"
        private val COLUMN_EVENT_DESCRIPTION = "event_description"
        private  val KEY_NAME = "image_name"
        private var KEY_IMAGE = "image_data"
    }

    // create table sql query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
              + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
              + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")



    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"



    ///creating table for events
    private val CREATE_EVENT_TABLE = ("CREATE TABLE " + TABLE_EVENT + "("
            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EVENT_NAME + " TEXT,"
            + COLUMN_EVENT_LOCATION + " TEXT," + COLUMN_EVENT_DESCRIPTION + " TEXT,"
            + KEY_IMAGE + " BLOB" +")")


    // drop table sql query EVENTS
    private val DROP_EVENT_TABLE = "DROP TABLE IF EXISTS $TABLE_EVENT"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_EVENT_TABLE)
    }

     /*
      super cool alternative
      override fun onCreate(db: SQLiteDatabase?) {

        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
      }*/

     override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
      //Drop User Table if exist
     db.execSQL(DROP_USER_TABLE)
     db.execSQL(DROP_EVENT_TABLE)
       // Create tables again
     onCreate(db)
   }


    fun getAllUser(): List<User> {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)

        // sorting orders
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<User>()

        val db = this.readableDatabase

        // query the user table
        val cursor = db.query(TABLE_USER, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = User(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)))

                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }


////////////// function to get all Events
    fun getAllEvent(): List<Event> {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_EVENT_ID, COLUMN_EVENT_NAME, COLUMN_EVENT_LOCATION, COLUMN_EVENT_DESCRIPTION, KEY_IMAGE)

        // sorting orders
        val sortOrder = "$COLUMN_EVENT_NAME ASC"
        val eventList = ArrayList<Event>()

        val db = this.readableDatabase

        // query the user table
        val cursor = db.query(TABLE_EVENT, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val event = Event(
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_NAME)),
                    location = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_LOCATION)),
                    description = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DESCRIPTION)),
                    KEY_IMAGE = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)))
                eventList.add(event)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return eventList
    }


    fun queryData(sql: String?) {
        val database = writableDatabase
        database.execSQL(sql)
    }



    fun addEvent(event: Event) {              //inserting user records

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EVENT_NAME, event.name)
        values.put(COLUMN_EVENT_LOCATION, event.location)
        values.put(COLUMN_EVENT_DESCRIPTION, event.description)
        //values.put(KEY_IMAGE, Utility.getBytes(event.getBitmap()))
        //values.put(KEY_NAME,  event.KEY_NAME)
        values.put(KEY_IMAGE,   event.KEY_IMAGE)
        // Inserting Row
        db.insert(TABLE_EVENT, null, values)
        db.close()
    }




    fun addUser(user: User) {              //inserting user records
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)
        // Inserting Row
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun updateUser(user: User) {                 //updating user records
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)
        // updating row
        db.update(TABLE_USER, values, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString()))
        db.close()
    }

    fun deleteUser(user: User) {                 // delete user records
        val db = this.writableDatabase
        // delete user record by id
        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?",
            arrayOf(user.id.toString()))
        db.close()
    }


    fun checkUser(email: String): Boolean {            // super cool method thanks internet ur the best
        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_USER_EMAIL = ?"
        // selection argument
        val selectionArgs = arrayOf(email)
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(TABLE_USER, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0) {
            return true
        }
        return false
    }


    fun checkEvent(name: String): Boolean {            // super cool method thanks internet ur the best
        // array of columns to fetch
        val columns = arrayOf(COLUMN_EVENT_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_EVENT_NAME = ?"
        // selection argument
        val selectionArgs = arrayOf(name)
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(TABLE_EVENT, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0) {
            return true
        }
        return false
    }

    /*
     * This method to check user exist or not
     * @param email
     * @param password
     * @return true/false
    fun checkUser(email: String, password: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"
        // selection arguments
        val selectionArgs = arrayOf(email, password)
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(TABLE_USER, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0)
            return true
        return false
    }*/




}




