package com.example.attendanceappstudent.network

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.attendanceappstudent.data_class.UserLoginRequest
import com.example.attendanceappstudent.data_class.UserLoginResponse
import com.example.attendanceappstudent.data_class.UserProfile
import com.example.attendanceappstudent.helper.ApiLinkHelper
import com.example.attendanceappteacher.data_class.StudentsAttendance
import com.example.attendenceappteacher.data_class.AddAttendance
import com.example.attendenceappteacher.data_class.AllStudentsOfAClass
import com.example.attendenceappteacher.data_class.MyClassResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class ApiClient private constructor(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)
    private val apiLinkHelper = ApiLinkHelper()

    companion object {
        @Volatile
        private var INSTANCE: ApiClient? = null

        fun getInstance(context: Context): ApiClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiClient(context).also { INSTANCE = it }
            }
        }
    }
    fun loginUser(
        user: UserLoginRequest,
        onSuccess: (UserLoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Create a JSON object for the request body
        val jsonBody = JSONObject().apply {
            put("uucms_id", user.uucmsId)
            put("password", user.password)
        }

        // Create a JsonObjectRequest
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, apiLinkHelper.loginUserApiUri(), jsonBody,
            { response ->
                Log.e("ApiClient", response.toString())
                try {
                    // Parse the JSON response using Gson
                    val loginResponse =
                        Gson().fromJson(response.toString(), UserLoginResponse::class.java)
                    onSuccess(loginResponse)
                } catch (e: Exception) {
                    Log.e("ApiClient", "Error parsing login response: ${e.message}")
                    onError("Error parsing response")
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorResponse = String(error.networkResponse.data)

                    Log.e("ApiClient", "Status Code: $statusCode")
                    Log.e("ApiClient", "Error Response: $errorResponse")

                    onError("Error $statusCode: $errorResponse")
                } else {
                    Log.e("ApiClient", "Error during login: ${error.message}")
                    onError(error.message ?: "Unknown network error occurred")
                }
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest)
    }

    fun getUserProfile(
        token: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,  // The method type, GET in this case
            apiLinkHelper.getUserProfileApiUri(),  // Your API URL
            null,  // No body for GET requests
            { response ->  // Success listener
                // Parse the response
                try {
                    val userProfile = Gson().fromJson(response.toString(), UserProfile::class.java)
                    Log.d("UserProfile", "User Profile: ${userProfile.first_name}, ${userProfile.email}")
                    onSuccess(userProfile)
                } catch (e: Exception) {
                    onError("Failed to parse the server response.")
                }
            },
            { error ->  // Error listener
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            // Add Authorization header with Bearer token
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest) // Add the request to the queue
    }

    fun getMyClasses(
        token: String,
        onSuccess: (MyClassResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET,  // HTTP GET method
            apiLinkHelper.getMyClassesApiUri(),  // Your API URL
            null,  // No body for GET requests
            { response ->  // Success listener
                try {
                    // Parse the JSON response into ClassEntity
                    val classes = Gson().fromJson(response.toString(), MyClassResponse::class.java)
                    Log.d("MyClasses", "Retrieved classes: $classes")
                    onSuccess(classes)
                } catch (e: Exception) {
                    onError("Failed to parse the server response.")
                }
            },
            { error ->  // Error listener
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            // Add Authorization header with Bearer token
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest)
    }

    fun getStudentsOfClass(
        token: String,
        classId: Int,
        onSuccess: (List<AllStudentsOfAClass>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = apiLinkHelper.getStudentsOfClassApiUri(classId) // Construct the URL

        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET,  // HTTP GET method
            url,  // Full API URL with classId
            null,  // No body for GET requests
            { response ->  // Success listener
                try {
                    // Parse the JSON response into a List of AllStudentsOfAClass
                    val students = Gson().fromJson(response.toString(), Array<AllStudentsOfAClass>::class.java).toList()
                    Log.d("StudentsOfClass", "Retrieved students: $students")
                    onSuccess(students)
                } catch (e: Exception) {
                    Log.e("StudentsOfClass", "Parsing error: ${e.message}")
                    onError("Failed to parse the server response.")
                }
            },
            { error ->  // Error listener
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            // Add Authorization header with Bearer token
            override fun getHeaders(): Map<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest)
    }

    fun markAttendance(
        token: String,
        classId: Long,
        subjectId: Long,
        schedulePeriod: Int,
        attendanceRecords: List<AddAttendance>,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Append query parameters to the URL
        val url = "${apiLinkHelper.markAttendanceApiUri()}?classId=$classId&subjectId=$subjectId&schedulePeriod=$schedulePeriod"

        // Prepare JSON body for attendanceRecords (Array of Objects)
        val jsonBody = JSONObject().apply {
            put("attendanceRecords", JSONArray(attendanceRecords.map {
                JSONObject().apply {
                    put("studentId", it.studentId)
                    put("isPresent", it.isPresent)
                }
            }))
        }

        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            { response -> // Success listener
                Log.d("MarkAttendance", "Response: $response")
                onSuccess(response) // Return the plain string response
            },
            { error -> // Error listener
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("MarkAttendance", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("MarkAttendance", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            // Add Authorization header with Bearer token
            override fun getHeaders(): Map<String, String> {
                return mapOf("Authorization" to "Bearer $token")
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        stringRequest.retryPolicy = DefaultRetryPolicy(
            30000,  // **30 seconds timeout**
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the request to the queue
        requestQueue.add(stringRequest)
    }

    fun getAttendanceSummary(
        token: String,
        classId: Int,
        subjectId: Int,
        onSuccess: (List<StudentsAttendance.StudentAttendanceItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url =  apiLinkHelper.getStudentAttendenceSummary(classId, subjectId)

        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    // Convert JSON response to a list of studentsAttendenceItem
                    val attendanceSummary = Gson().fromJson(
                        response.toString(),
                        Array<StudentsAttendance.StudentAttendanceItem>::class.java
                    ).toList()

                    Log.d("AttendanceSummary", "Retrieved attendance data: $attendanceSummary")
                    onSuccess(attendanceSummary)
                } catch (e: Exception) {
                    Log.e("AttendanceSummary", "Parsing error: ${e.message}")
                    onError("Failed to parse the server response.")
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val errorResponse = String(error.networkResponse.data)
                    Log.e("ApiClient", "Error Response: $errorResponse")
                    onError(errorResponse)
                } else {
                    Log.e("ApiClient", "Unknown Error: ${error.message}")
                    onError(error.message ?: "Unknown error occurred.")
                }
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf("Authorization" to "Bearer $token")
            }
        }

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest)
    }
}