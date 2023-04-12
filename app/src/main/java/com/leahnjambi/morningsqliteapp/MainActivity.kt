package com.leahnjambi.morningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtIdNumber: EditText
    lateinit var btnSave: Button
    lateinit var btnView: Button
    lateinit var btnDelete: Button
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtId)
        btnSave = findViewById(R.id.mBtnSave)
        btnDelete = findViewById(R.id.mBtnDelete)
        btnView = findViewById(R.id.mBtnView)

        // Create database called eMobilisDb
        db = openOrCreateDatabase("eMobilisDb", Context.MODE_PRIVATE, null)
        // Create a table called users inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        // Set on click listeners to the buttons
        btnSave.setOnClickListener {
            // Receive the data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            // Check if the user is submitting the empty fields
            if (name.isEmpty() || email.isEmpty() || idNumber.isEmpty()) {
                // Display an error message using the defined message function
                message("EMPTY FIELDS!!!", "Please fill all the inputs!!")
            } else {
                //Proceed to save the data
                db.execSQL("INSERT INTO users VALUES('" + name + "','" + email + "','" + idNumber + "')")
                clear()
                message("SUCCESS!!", "User saved successfully")
            }

        }
        btnView.setOnClickListener {
            //Use cursor to select all users
            var cursor = db.rawQuery("SELECT * FROM users", null)
            //Check if there's any record in the db
            if (cursor.count == 0) {
                message("NO RECORDS!!", "Sorry, no users were found!!!")
            } else {
                //Use string buffer to append all the available records using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()) {
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName + "\n")
                    buffer.append(retrievedEmail + "\n")
                    buffer.append(retrievedIdNumber + "\n\n")

                }
                message("USERS", buffer.toString())
            }
        }
        btnDelete.setOnClickListener {
            val idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()) {
                message("EMPTY FIEDS", "Please enter an ID number")
            } else {
                // Use cursor to select the user with the given ID
                var cursor =
                    db.rawQuery("SELECT * FROM users WHERE kitambulisho='" + idNumber + "'", null)
                if (cursor.count == 0) {
                    message("NO RECORDS!", "Sorry , no user with id" + idNumber)
                } else {
                    //Proceed to delete the user
                    db.execSQL("DELETE FROM user WHERE kitambulisho='" + idNumber + "' ")
                    clear()
                    message("SUCCESS!!!", "User deleted successfully")
                }
            }
        }
    }

    fun message(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Close", null)
        alertDialog.create().show()
    }

    fun clear() {
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")
    }

}