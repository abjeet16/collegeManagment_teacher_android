package com.example.attendenceappteacher.data_class

data class AttendanceRequest(
    val classId: Long,
    val subjectId: Long,
    val schedulePeriod: Int,
    val attendanceRecords: List<AddAttendanceRequest>
)

data class AddAttendanceRequest(
    val studentId: String,
    val status: Boolean
)
