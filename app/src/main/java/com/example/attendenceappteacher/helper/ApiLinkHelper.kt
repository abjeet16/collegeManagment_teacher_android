package com.example.attendanceappstudent.helper

class ApiLinkHelper {
    val BASE_URL: String = "http://abjeet.ap-south-1.elasticbeanstalk.com/api/v1/"

    fun loginUserApiUri(): String {
        return BASE_URL + "auth/user/login"
    }

    fun getUserProfileApiUri(): String{
        return BASE_URL+"User/my_profile"
    }

    fun getMyClassesApiUri(): String {
        return BASE_URL+"teacher/my_classes"
    }

    fun getStudentsOfClassApiUri(classId: Int): String {
        return BASE_URL + "teacher/$classId/students"
    }

    fun markAttendanceApiUri(): String {
        return BASE_URL + "teacher/mark_attendance"
    }

    fun getStudentAttendenceSummary( classId: Int,subjectId: Int):String{
        return BASE_URL+"teacher/attendance/${classId}/${subjectId}"
    }
}