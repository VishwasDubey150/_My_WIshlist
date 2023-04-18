package com.example.mywishlist.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.mywishlist.model.MyWishlistModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "MyWishlistDatabase" // Database name
        private const val TABLE_MYWISHLIST = "WishlistsTable" // Table Name

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_MYWISHLIST_TABLE = ("CREATE TABLE " + TABLE_MYWISHLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_MYWISHLIST_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_MYWISHLIST")
        onCreate(db)
    }

    fun addWishlist(Wishlist: MyWishlistModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, Wishlist.title) // WishlistModelClass TITLE
        contentValues.put(KEY_IMAGE, Wishlist.image) // WishlistModelClass IMAGE
        contentValues.put(
            KEY_DESCRIPTION,
            Wishlist.description
        ) // WishlistModelClass DESCRIPTION
        contentValues.put(KEY_DATE, Wishlist.date) // WishlistModelClass DATE

        // Inserting Row
        val result = db.insert(TABLE_MYWISHLIST, null, contentValues)
        //2nd argument is String containing nullColumnHack  

        db.close() // Closing database connection
        return result
    }


    fun deletePlace(myWishlistModel: MyWishlistModel): Int {
        val db=this.writableDatabase
        val success=db.delete(TABLE_MYWISHLIST, KEY_ID+"="+myWishlistModel.id,null)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getWishlistsList(): ArrayList<MyWishlistModel> {

        // A list is initialize using the data model class in which we will add the values from cursor.
        val WishlistList: ArrayList<MyWishlistModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_MYWISHLIST" // Database select query

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = MyWishlistModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE))
                    )
                    WishlistList.add(place)

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return WishlistList
    }
    // END
}
    //