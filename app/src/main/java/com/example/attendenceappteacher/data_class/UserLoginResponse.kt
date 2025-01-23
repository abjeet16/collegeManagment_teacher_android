package com.example.attendanceappstudent.data_class

data class UserLoginResponse(
    val token: String,
    val lastName: String,
    val firstName: String,
    val username: String
)
