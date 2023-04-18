package com.example.mywishlist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mywishlist.R
import com.example.mywishlist.model.MyWishlistModel

class place_details : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );
        supportActionBar?.hide()

        var myWishlistModel : MyWishlistModel?=null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS))
        {
            myWishlistModel=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as MyWishlistModel
        }
        if (myWishlistModel != null)
        {
            var cover=findViewById<ImageView>(R.id.cover)
            var name=findViewById<TextView>(R.id.name)
            var description=findViewById<TextView>(R.id.dis)
            var exp_date=findViewById<Button>(R.id.exp_date)

            //cover.setImageURI(Uri.parse(myWishlistModel.image))
            name.text=myWishlistModel.title
            description.text=myWishlistModel.description
            //location.text=myWishlistModel.location
            var e_date=myWishlistModel.date
            exp_date.setOnClickListener {
                Toast.makeText(this,"$e_date",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun backing(view: View) {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun exp_date(view: View) {
        Toast.makeText(this,"expected date",Toast.LENGTH_SHORT).show()
    }


}