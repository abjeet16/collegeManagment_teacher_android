package com.example.attendenceappteacher.data_class


import com.google.gson.annotations.SerializedName

class ClassEntity : ArrayList<ClassEntity.ClassEntityItem>(){
    data class ClassEntityItem(
        @SerializedName("batchYear")
        val batchYear: Int?,
        @SerializedName("course")
        val course: Course?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("section")
        val section: String?
    ) {
        data class Course(
            @SerializedName("courseName")
            val courseName: String?,
            @SerializedName("id")
            val id: Int?
        )
    }
}