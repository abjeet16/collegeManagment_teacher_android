package com.example.attendenceappteacher.adapter

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

    inner class AttendanceViewHolder(private val binding: AttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: AllStudentsOfAClass) {
            // Initialize the attendance state if not already set
            if (!attendanceMap.containsKey(student.studentId)) {
                attendanceMap[student.studentId] = false // Default to absent
            }

            binding.student = student
            binding.toggleAttendance.isChecked = attendanceMap[student.studentId] ?: false

            // Update the attendanceMap when the toggle state changes
            binding.toggleAttendance.setOnCheckedChangeListener { _, isChecked ->
                attendanceMap[student.studentId] = isChecked
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

    // Method to get the final attendance map when submitting
    fun submitAttendance(): Map<String, Boolean> {
        return attendanceMap
    }
}

