package com.example.mywishlist.model
import java.io.Serializable
class MyWishlistModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String
): Serializable