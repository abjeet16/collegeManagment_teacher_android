package com.example.attendenceappteacher.ui.attendence

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendenceappteacher.adapter.AttendanceAdapter
import com.example.attendenceappteacher.data_class.AddAttendance
import com.example.attendenceappteacher.data_class.AllStudentsOfAClass
import com.example.attendenceappteacher.databinding.FragmentAttendenceBinding

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendenceBinding? = null
    private val binding get() = _binding!!

    // Retrieve arguments passed from HomeFragment using Safe Args
    private val args: AttendanceFragmentArgs by navArgs()
    private lateinit var sharedPreferences : SharedPreferences
    private var classId: Long = -1
    private var subjectId : Long = -1
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the variables passed from HomeFragment
        classId = args.classId
        subjectId = args.subjectId

        sharedPreferences = requireActivity().getSharedPreferences("UserSession", MODE_PRIVATE)

        token = sharedPreferences.getString("token", null).toString()
        if (token.isNotBlank()) {
            getAllStudentsOfClass(token,classId)
        }
    }

    private fun getAllStudentsOfClass(token: String, classId: Long) {
        ApiClient.getInstance(requireContext()).getStudentsOfClass(
            token = token,
            classId = classId.toInt(),
            onSuccess = { students ->
                setRecyclerview(students)
            },
            onError = { errorMessage ->
                Log.e("AttendanceFragment", "Error: $errorMessage")
                Toast.makeText(requireContext(), "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setRecyclerview(students: List<AllStudentsOfAClass>) {
        val adapter = AttendanceAdapter(students, ::submitAttendance)
        binding.rvAttendance.adapter = adapter
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.btnSubmitAttendance.setOnClickListener {
            val attendance = adapter.submitAttendance()
            submitAttendance(attendance)
        }
    }

    private fun submitAttendance(attendance: Map<String, Boolean>) {
        val addAttendance = ArrayList<AddAttendance>()
        for ((studentId, isPresent) in attendance) {
            addAttendance.add(AddAttendance(studentId, isPresent))
        }
        ApiClient.getInstance(requireContext()).markAttendance(
            token = token,
            classId = 101,
            subjectId = 202,
            schedulePeriod = 1,
            attendanceRecords = addAttendance,
            onSuccess = { message ->
                Log.d("AttendanceAPI", message)
            },
            onError = { error ->
                Log.e("AttendanceAPI", error)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
