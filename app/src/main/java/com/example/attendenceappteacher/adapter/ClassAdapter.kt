package com.example.attendenceappteacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceappteacher.data_class.ClassEntity
import com.example.attendenceappteacher.databinding.ItemClassBinding

class ClassAdapter(
    private val classList: List<ClassEntity.ClassEntityItem>
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    // ViewHolder using View Binding
    inner class ClassViewHolder(val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classList[position]
        holder.binding.classItem = classItem // Set the data in binding
        holder.binding.executePendingBindings() // Notify binding to refresh immediately
    }

    override fun getItemCount(): Int = classList.size
}
