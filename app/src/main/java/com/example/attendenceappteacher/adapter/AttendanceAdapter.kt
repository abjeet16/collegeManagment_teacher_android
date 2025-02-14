package com.example.attendenceappteacher.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceappteacher.data_class.AllStudentsOfAClass
import com.example.attendenceappteacher.databinding.AttendanceItemBinding

class AttendanceAdapter(
    private val students: List<AllStudentsOfAClass>,
    private val onSubmitAttendance: (Map<String, Boolean>) -> Unit
) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private val attendanceMap = mutableMapOf<String, Boolean>()

    init {
        // Ensure every student is present in the map
        students.forEach { student ->
            if (!attendanceMap.containsKey(student.studentId)) {
                attendanceMap[student.studentId] = false // Default to absent
            }
        }
    }

    inner class AttendanceViewHolder(private val binding: AttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: AllStudentsOfAClass) {
            // Log each student when binding
            Log.d("AttendanceAdapter", "Binding student: ${student.studentName} (ID: ${student.studentId})")

            binding.student = student
            binding.toggleAttendance.isChecked = attendanceMap[student.studentId] ?: false

            // Update attendanceMap when checkbox changes
            binding.toggleAttendance.setOnCheckedChangeListener { _, isChecked ->
                attendanceMap[student.studentId] = isChecked
                Log.d("AttendanceAdapter", "Updated attendance for ${student.studentName}: $isChecked")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = AttendanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size

    fun submitAttendance(): Map<String, Boolean> {
        Log.d("AttendanceAdapter11", "Submitting Attendance - Total students: ${students.size}")

        students.forEach { student ->
            val isPresent = attendanceMap[student.studentId] ?: false
            Log.d("AttendanceAdapter11", "Student: ${student.studentName} (ID: ${student.studentId}) - Present: $isPresent")
        }

        return attendanceMap
    }
}


