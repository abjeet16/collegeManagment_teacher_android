package com.example.attendenceappteacher.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceappstudent.network.ApiClient
import com.example.attendenceappteacher.adapter.ClassAdapter
import com.example.attendenceappteacher.data_class.ClassEntity
import com.example.attendenceappteacher.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireActivity().getSharedPreferences("UserSession", MODE_PRIVATE)

        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            getTeacherClasses(token)
        }
        return root
    }

    private fun getTeacherClasses(token: String) {
        ApiClient.getInstance(requireContext()).getMyClasses(
            token = token,
            onSuccess = { teacherClasses ->
                setUpClassData(teacherClasses)
            },
            onError = { errorMessage ->
                Log.e("UserProfile", "Error: $errorMessage")
                Toast.makeText(requireContext(), "Failed to fetch user profile: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setUpClassData(teacherClasses: ClassEntity) {
        val adapter = ClassAdapter(teacherClasses)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}