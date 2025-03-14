package com.example.attendanceappteacher.data_class

import com.google.gson.annotations.SerializedName

public class StudentsAttendance : ArrayList<StudentsAttendance.StudentAttendanceItem>() {
    data class StudentAttendanceItem(
        @SerializedName("studentName")
        val studentName: String? = null,

        @SerializedName("studentId")
        val studentId: String? = null,

        @SerializedName("percentage")
        val percentage: Double? = null
    )
}

