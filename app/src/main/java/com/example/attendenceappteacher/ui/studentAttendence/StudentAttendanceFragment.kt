package com.example.attendenceappteacher.ui.studentAttendence

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendanceappteacher.data_class.StudentsAttendance
import com.example.attendenceappteacher.adapter.StudentAttendanceAdapter
import com.example.attendenceappteacher.databinding.FragmentStudentAttendenceBinding
import com.example.attendenceappteacher.ui.attendence.AttendanceFragmentArgs
import kotlin.math.log

class StudentAttendanceFragment : Fragment() {

    private var _binding: FragmentStudentAttendenceBinding? = null
    private val binding get() = _binding!!
    private val args: AttendanceFragmentArgs by navArgs()

    private lateinit var sharedPreferences : SharedPreferences

    private var classId: Long = -1
    private var subjectId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        classId = args.classId
        subjectId = args.subjectId

        if (classId < 0 || subjectId < 0) {
            Log.d("hibbjabc",(classId.toString() + " " + subjectId) )
            Toast.makeText(requireContext(), "Invalid class or subject information.", Toast.LENGTH_SHORT).show()
            return
        }
        sharedPreferences = requireActivity().getSharedPreferences("UserSession", MODE_PRIVATE)

        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            getStudentSummary(token)
        }
    }

    private fun getStudentSummary(token: String) {
        ApiClient.getInstance(requireContext()).getAttendanceSummary(
            token = token,
            classId = classId.toInt(),
            subjectId = subjectId.toInt(),
            onSuccess = { studentsAttendenceItems ->
                setUpRecyclerView(studentsAttendenceItems)
            },
            onError = { errorMessage ->
                Log.e("UserProfile", "Error: $errorMessage")
                Toast.makeText(requireContext(), "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setUpRecyclerView(studentsAttendenceItems: List<StudentsAttendance.StudentAttendanceItem>) {
        val adapter = StudentAttendanceAdapter(studentsAttendenceItems)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentAttendenceBinding.inflate(inflater, container, false)

        // Display retrieved classId and subjectId
        Log.d("hibbjabc",(classId.toString() + " " + subjectId) )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
