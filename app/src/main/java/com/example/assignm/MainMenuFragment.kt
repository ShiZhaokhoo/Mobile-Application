package com.example.assignm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.assignm.IssuedMaterial.IssuedViewModel
import com.example.assignm.ReceivedMaterial.ReceivedViewModel
import com.example.assignm.RestockMaterial.RestockViewModel
import com.example.assignm.ReturnMaterial.ReturnViewModel
import com.example.assignm.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_main_menu,container,false)

        Database.getPreviousRecord()

        binding.receivedButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainMenuFragment_to_receivedFragment)
        }

        binding.restockButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainMenuFragment_to_restockFragment)
        }

        binding.issuedButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainMenuFragment_to_issuedFragment)
        }

        binding.returnButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainMenuFragment_to_returnFragment)
        }

        binding.receivedTransactionNumber.text = Material.getReceivePerDay().toString()
        binding.issuedTransactionNumber.text = Material.getIssuedPerDay().toString()

        // prevent on back press happen
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.d("tag","back button pressed")    // Handle the back button event
        }

        return binding.root
    }


}