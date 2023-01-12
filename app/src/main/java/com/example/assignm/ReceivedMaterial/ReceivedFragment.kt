package com.example.assignm.ReceivedMaterial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.R
import com.example.assignm.databinding.FragmentReceivedBinding

private const val RECEIVED_MODULE_TYPE = 1

class ReceivedFragment : Fragment(){
    private lateinit var binding: FragmentReceivedBinding
    private lateinit var viewModel: ReceivedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_received,container,false)

        viewModel = ViewModelProvider(requireActivity()).get(ReceivedViewModel::class.java)

        if(viewModel.getError() == true){
            Toast.makeText(context, "Invalid Input, Please Scan Again!", Toast.LENGTH_SHORT).show()
        }

        binding.receivedPartNumberEdit.text = viewModel.getPartNumber()
        binding.receivedQuantityEdit.text = viewModel.getQuantity().toString()
        binding.receivedPICEdit.setText(viewModel.getPic())

        binding.receivedPartScanButton.setOnClickListener {
            val action = ReceivedFragmentDirections.actionReceivedFragmentToScannerFragment(
                RECEIVED_MODULE_TYPE,true)
            findNavController().navigate(action)
        }

        binding.receivedQuantityScanButton.setOnClickListener {
            val action = ReceivedFragmentDirections.actionReceivedFragmentToScannerFragment(
                RECEIVED_MODULE_TYPE, false)
            findNavController().navigate(action)
        }

        binding.receivedConfirmButton.setOnClickListener {
            if(viewModel.getPartNumber()==""){
                Toast.makeText(context, "Part Number Is Empty", Toast.LENGTH_SHORT).show()
            }
            else if(viewModel.getQuantity()==0){
                Toast.makeText(context, "Quantity Is Empty", Toast.LENGTH_SHORT).show()
            }
            else if(viewModel.getPic() == ""){
                Toast.makeText(context, "PIC Is Empty", Toast.LENGTH_SHORT).show()
            }
            else{
                Database.addMaterial(
                    viewModel.getPartNumber()!!,
                    viewModel.getQuantity()!!,
                    binding.receivedPICEdit.text.toString()
                )
                val action = ReceivedFragmentDirections.actionReceivedFragmentToReceiveReceiptFragment(viewModel.getPartNumber()!!, RECEIVED_MODULE_TYPE)
                findNavController().navigate(action)
                viewModel.clear()
            }
        }

        binding.receivedPICEdit.doAfterTextChanged {
            viewModel.setPic(binding.receivedPICEdit.text.toString())
        }

        return binding.root
    }



}