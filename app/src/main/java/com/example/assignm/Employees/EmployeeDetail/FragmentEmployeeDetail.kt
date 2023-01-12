package com.example.assignm.Employees.EmployeeDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignm.Database
import com.example.assignm.R
import com.example.assignm.ScannerFragmentArgs
import com.example.assignm.databinding.FragmentEmployeeDetailBinding

class FragmentEmployeeDetail : Fragment() {

    private lateinit var binding: FragmentEmployeeDetailBinding
    private val args: FragmentEmployeeDetailArgs by navArgs<FragmentEmployeeDetailArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee_detail, container,false)

        val employeePosition = args.empIndex

        binding.profileEmailAddressEdit.text = Database.employees[employeePosition].getEmail()
        binding.profileEmployeeIdEdit.text = Database.employees[employeePosition].getEmpId()
        binding.profileNameEdit.text = Database.employees[employeePosition].getName()
        binding.profileTelNoEdit.text = Database.employees[employeePosition].getTelNo()
        binding.profilePositionEdit.text = Database.employees[employeePosition].getPosition()

        binding.profileConfirmButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentEmployeeDetail_to_mainMenuFragment)
        }

        return binding.root
    }
}