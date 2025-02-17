package com.example.attendenceappteacher.ui.attendence

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    private val args: AttendanceFragmentArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences
    private var classId: Long = -1
    private var subjectId: Long = -1
    private lateinit var token: String
    private var scheduledPeriod:Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classId = args.classId
        subjectId = args.subjectId

        if (classId < 0 || subjectId < 0) {
            Toast.makeText(requireContext(), "Invalid class or subject information.", Toast.LENGTH_SHORT).show()
            return
        }

        sharedPreferences = requireActivity().getSharedPreferences("UserSession", MODE_PRIVATE)
        token = sharedPreferences.getString("token", null) ?: ""

        if (token.isNotBlank()) {
            getAllStudentsOfClass(token, classId)
        } else {
            Toast.makeText(requireContext(), "Authentication required. Please log in.", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Failed to fetch students: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setRecyclerview(students: List<AllStudentsOfAClass>) {
        val adapter = AttendanceAdapter(students, ::submitAttendance)
        binding.rvAttendance.adapter = adapter
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())

        submitButtonClickListener(adapter)
    }

    private fun submitButtonClickListener(adapter: AttendanceAdapter) {
        binding.btnSubmitAttendance.setOnClickListener {
            val scheduledPeriodText = binding.etScheduledPeriod.text.toString()

            if (scheduledPeriodText.isBlank()) {
                binding.etScheduledPeriod.error = "Please enter a valid period"
                return@setOnClickListener
            }

            scheduledPeriod = scheduledPeriodText.toInt()

            if (scheduledPeriod <= 0) {
                binding.etScheduledPeriod.error = "Please enter a valid period greater than 0"
                return@setOnClickListener
            }

            binding.btnSubmitAttendance.isEnabled = false
            val attendance = adapter.submitAttendance()
            submitAttendance(attendance)
        }
    }

    private fun submitAttendance(attendance: Map<String, Boolean>) {
        val addAttendance = attendance.map { AddAttendance(it.key, it.value) }

        // Disable button to prevent multiple clicks
        binding.btnSubmitAttendance.isEnabled = false

        ApiClient.getInstance(requireContext()).markAttendance(
            token = token,
            classId = classId,
            subjectId = subjectId,
            schedulePeriod = scheduledPeriod,
            attendanceRecords = addAttendance,
            onSuccess = { message ->
                Log.d("AttendanceAPI", message)
                Toast.makeText(requireContext(), "Attendance marked successfully!", Toast.LENGTH_SHORT).show()

                // Re-enable button after success
                binding.btnSubmitAttendance.isEnabled = true
                findNavController().popBackStack()
            },
            onError = { error ->
                Log.e("AttendanceAPI", error)
                Toast.makeText(requireContext(), "Failed to mark attendance: $error", Toast.LENGTH_SHORT).show()

                // Re-enable button after failure
                binding.btnSubmitAttendance.isEnabled = true
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

