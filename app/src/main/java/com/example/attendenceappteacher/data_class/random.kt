package com.example.attendenceappteacher.data_class


import com.google.gson.annotations.SerializedName

class random : ArrayList<random.randomItem>(){
    data class randomItem(
        @SerializedName("classEntity")
        val classEntity: ClassEntity?,
        @SerializedName("subject")
        val subject: Subject?
    ) {
        data class ClassEntity(
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
    
        data class Subject(
            @SerializedName("courses")
            val courses: Courses?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("subjectId")
            val subjectId: String?,
            @SerializedName("subjectName")
            val subjectName: String?
        ) {
            data class Courses(
                @SerializedName("courseName")
                val courseName: String?,
                @SerializedName("id")
                val id: Int?
            )
        }
    }
}