package com.example.attendanceappstudent.helper

class ApiLinkHelper {
    val BASE_URL: String = "https://collegemanagment-springboot-production.up.railway.app/api/v1/"

    fun loginUserApiUri(): String {
        return BASE_URL + "auth/user/login"
    }

    fun getUserProfileApiUri(): String? {
        return BASE_URL+"User/my_profile"
    }

    fun getMyClassesApiUri(): String? {
        return BASE_URL+"teacher/my_classes"
    }

    fun getStudentsOfClassApiUri(classId: Int): String {
        return BASE_URL + "teacher/$classId/students"
    }

    fun markAttendanceApiUri(): String {
        return BASE_URL + "teacher/mark_attendance"
    }
}