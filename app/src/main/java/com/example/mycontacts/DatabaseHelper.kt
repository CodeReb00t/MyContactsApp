package com.example.mycontacts

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "contacts_db", null, 1) {

    private val tag = "DatabaseHelper"

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE contacts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun addContact(contact: Contact) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", contact.name)
            put("phone", contact.phoneNumber)
        }

        try {
            val result = db.insert("contacts", null, contentValues)
            if (result == -1L) {
                Log.e(tag, "Error adding contact: ${contact.name}")
            } else {
                Log.d(tag, "Contact added: ${contact.name}, ${contact.phoneNumber}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error adding contact: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun getAllContacts(): List<Contact> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM contacts", null)
        val contacts = mutableListOf<Contact>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val phone = cursor.getString(cursor.getColumnIndex("phone"))
            contacts.add(Contact(name, phone))
        }
        cursor.close()
        db.close()

        return contacts
    }
}
