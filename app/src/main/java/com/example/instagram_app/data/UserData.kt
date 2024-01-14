package com.example.instagram_app.data

data class UserData(
    var userID: String? = null,
    var name: String? = null,
    var userName: String? = null,
    var imageUrl: String? = null,
    var bio: String? = null,
    var following: List<String>? = null,
){
fun toMap() = mapOf(
    "name" to name,
    "userID" to userID,
    "userName" to userName,
    "imageUrl" to imageUrl,
    "bio" to bio,
    "following" to following,
)
}
