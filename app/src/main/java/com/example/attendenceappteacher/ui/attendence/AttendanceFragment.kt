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
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendenceappteacher.databinding.FragmentAttendenceBinding

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendenceBinding? = null
    private val binding get() = _binding!!

    // Retrieve arguments passed from HomeFragment using Safe Args
    private val args: AttendanceFragmentArgs by navArgs()
    private lateinit var sharedPreferences : SharedPreferences

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
        val classId = args.classId
        val subjectId = args.subjectId

        sharedPreferences = requireActivity().getSharedPreferences("UserSession", MODE_PRIVATE)

        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            getAllStudentsOfClass(token,classId)
        }
    }

    private fun getAllStudentsOfClass(token: String, classId: Long) {
        ApiClient.getInstance(requireContext()).getStudentsOfClass(
            token = token,
            classId = classId.toInt(),
            onSuccess = { students ->
                Log.d("AttendanceFragment", "Students: $students")
            },
            onError = { errorMessage ->
                Log.e("AttendanceFragment", "Error: $errorMessage")
                Toast.makeText(requireContext(), "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
