package com.example.attendenceappteacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappteacher.data_class.StudentsAttendance
import com.example.attendenceappteacher.databinding.StudentsAttdenceItemBinding

class StudentAttendanceAdapter(private val students: List<StudentsAttendance.StudentAttendanceItem>) :
    RecyclerView.Adapter<StudentAttendanceAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: StudentsAttdenceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = StudentsAttdenceItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.binding.student = student  // Binding data
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = students.size
}
