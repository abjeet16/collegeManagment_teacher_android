package com.example.attendenceappteacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendenceappteacher.data_class.MyClassResponse
import com.example.attendenceappteacher.databinding.ItemClassBinding

class ClassAdapter(
    private val classList: MyClassResponse,
    private val onItemClick: (MyClassResponse, Int) -> Unit,
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    // ViewHolder using View Binding
    inner class ClassViewHolder(val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.binding.classItem = classList.get(position) // Set the data in binding
        holder.binding.root.setOnClickListener {
            onItemClick(classList,position)
        }
        holder.binding.executePendingBindings() // Notify binding to refresh immediately
    }

    override fun getItemCount(): Int = classList.size
}
