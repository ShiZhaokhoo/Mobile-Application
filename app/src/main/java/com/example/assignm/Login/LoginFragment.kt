package com.example.assignm.Login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.assignm.Database
import com.example.assignm.MainMenuActivity
import com.example.assignm.Material
import com.example.assignm.R
import com.example.assignm.databinding.FragmentLoginBinding
import com.google.android.material.tabs.TabLayout

class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //daily receive, monthly yearly, issued daily monthly yearly


        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login,container,false)


        var getDatabase = false
        binding.editLoginEmployeeId.doAfterTextChanged {
            if(!getDatabase){
                if(Database.materials.size >= 1){
                    Database.checkDate()
                }
                getDatabase = true
            }
        }

        binding.signInButton.setOnClickListener {
            Log.w(TAG, Database.materials.size.toString())
            login()
        }

        return binding.root
    }

    fun login(){
        var loginUnsucess: Boolean = false
        for(employee in Database.employees){
            if(employee.getEmpId() == binding.editLoginEmployeeId.text.toString() && employee.getPassword() == binding.editPassword.text.toString()){
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), MainMenuActivity::class.java)
                intent.putExtra("POSITION", employee.getPosition())
                startActivity(intent)
                loginUnsucess = true
            }
        }
        if(!loginUnsucess){
            Toast.makeText(context, "Employee ID/ Password Wrong!!!", Toast.LENGTH_SHORT).show()
        }
    }

}