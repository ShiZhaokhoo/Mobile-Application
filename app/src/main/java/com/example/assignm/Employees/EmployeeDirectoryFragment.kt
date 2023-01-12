package com.example.assignm.Employees

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.R
import com.example.assignm.databinding.FragmentEmployeeDirectoryBinding

class EmployeeDirectoryFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<EmployeesDirectoryAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentEmployeeDirectoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee_directory,container,false)

        layoutManager = LinearLayoutManager(context)

        binding.recycleViewEmployees.layoutManager = layoutManager

        EmployeesDirectoryAdapter.setFragment(this)

        adapter = EmployeesDirectoryAdapter()
        binding.recycleViewEmployees.adapter = adapter

        return binding.root
    }

    fun toDetail(index: Int){
        val action = EmployeeDirectoryFragmentDirections.actionEmployeeDirectoryFragmentToFragmentEmployeeDetail(index)
        findNavController().navigate(action)
    }
}