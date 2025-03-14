package com.example.attendenceappteacher.ui.AttendanceMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.attendenceappteacher.R
import com.example.attendenceappteacher.databinding.FragmentAttendanceMenuBinding

class AttendanceMenuFragment : Fragment() {

    private var _binding: FragmentAttendanceMenuBinding? = null
    private val binding get() = _binding!!

    private var classId: Long = -1
    private var subjectId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            classId = it.getLong("classId", -1)
            subjectId = it.getLong("subjectId", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMarkAttendance.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("classId", classId)
                putLong("subjectId", subjectId)
            }
            findNavController().navigate(R.id.action_attendanceMenuFragment_to_attendenceFragment, bundle)
        }

        binding.btnViewAttendance.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("classId", classId)
                putLong("subjectId", subjectId)
            }
            findNavController().navigate(R.id.action_attendanceMenuFragment_to_studentAttendenceFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

