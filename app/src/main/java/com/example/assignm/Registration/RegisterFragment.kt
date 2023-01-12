package com.example.assignm.Registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.R
import com.example.assignm.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register,container,false)

        binding.registerCancelButton.setOnClickListener {
            clear()
            Toast.makeText(context, "Register Cancelled!",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.registerConfirmButton.setOnClickListener {
            if(binding.registerNameEdit.text.toString() == ""){
                Toast.makeText(context, "Name Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerNameEdit.requestFocus()
                showKeyboard(binding.registerNameEdit)
            }else if(binding.registerTelNoEdit.text.toString() == ""){
                Toast.makeText(context, "Tel. No. Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerTelNoEdit.requestFocus()
                showKeyboard(binding.registerTelNoEdit)
            }else if(binding.registerEmailAddressEdit.text.toString() == ""){
                Toast.makeText(context, "Email Address Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerEmailAddressEdit.requestFocus()
                showKeyboard(binding.registerEmailAddressEdit)
            }else if(binding.registerPositionEdit.text.toString() == ""){
                Toast.makeText(context, "Position Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerPositionEdit.requestFocus()
                showKeyboard(binding.registerPositionEdit)
            }else if(binding.registerEmployeeIdEdit.text.toString() == ""){
                Toast.makeText(context, "Employee ID Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerEmployeeIdEdit.requestFocus()
                showKeyboard(binding.registerEmployeeIdEdit)
            }else if(binding.registerPasswordEdit.text.toString() == ""){
                Toast.makeText(context, "Password Is Empty", Toast.LENGTH_SHORT).show()
                binding.registerPasswordEdit.requestFocus()
                showKeyboard(binding.registerPasswordEdit)
            }else if(binding.registerConfirmPasswordEdit.text.toString() != binding.registerPasswordEdit.text.toString()){
                Toast.makeText(context, "Confirm Password Difference With Password", Toast.LENGTH_SHORT).show()
                binding.registerConfirmPasswordEdit.requestFocus()
                showKeyboard(binding.registerConfirmPasswordEdit)
            }
            else{
                Database.addEmployee(binding.registerNameEdit.text.toString(),binding.registerTelNoEdit.text.toString(),binding.registerEmailAddressEdit.text.toString(),
                    binding.registerPositionEdit.text.toString(),binding.registerEmployeeIdEdit.text.toString(),binding.registerPasswordEdit.text.toString())
                    Toast.makeText(context, "Register Successfully!",Toast.LENGTH_SHORT).show()
                    it.findNavController().navigate(R.id.action_registerFragment_to_mainMenuFragment)
            }
        }
        return binding.root
    }

    fun showKeyboard(view: View){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun clear(){
        binding.registerConfirmPasswordEdit.setText("")
        binding.registerEmailAddressEdit.setText("")
        binding.registerEmployeeIdEdit.setText("")
        binding.registerNameEdit.setText("")
        binding.registerPasswordEdit.setText("")
        binding.registerPositionEdit.setText("")
        binding.registerTelNoEdit.setText("")
    }
}