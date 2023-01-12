package com.example.assignm.RestockMaterial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.Material
import com.example.assignm.R
import com.example.assignm.databinding.FragmentRestockBinding

const val RESTOCK_MODULE_TYPE = 2

class RestockFragment : Fragment() {
    private lateinit var binding: FragmentRestockBinding
    private lateinit var viewModel: RestockViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restock,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(RestockViewModel::class.java)

        initialStep()

        binding.scanSerialButton.setOnClickListener {
            var action = RestockFragmentDirections.actionRestockFragmentToScannerFragment(
                RESTOCK_MODULE_TYPE,true)
            findNavController().navigate(action)
        }

        binding.restockRackIdButton.setOnClickListener {
            var action = RestockFragmentDirections.actionRestockFragmentToScannerFragment(
                RESTOCK_MODULE_TYPE,false)
            findNavController().navigate(action)
        }

        binding.restockPICEdit.doAfterTextChanged{
            viewModel.setPic(binding.restockPICEdit.text.toString())
        }

        binding.restockRackIdEdit.doAfterTextChanged{
            viewModel.setRackNumber(binding.restockRackIdEdit.text.toString())
        }

        binding.restockConfirmButton.setOnClickListener {
            if (binding.serialNumberEdit.text.toString() == ""){
                Toast.makeText(context, "Serial Number Is Empty!", Toast.LENGTH_SHORT).show()
            }else if (binding.restockRackIdEdit.text.toString() == ""){
                Toast.makeText(context, "Rack Number Is Empty!", Toast.LENGTH_SHORT).show()
            }else if (binding.restockPICEdit.text.toString() == ""){
                Toast.makeText(context, "PIC Is Empty!", Toast.LENGTH_SHORT).show()
            }
            else{
                Database.restockMaterial(
                    viewModel.getPartNumber()!!,
                    viewModel.getSerialNumber()!!,
                    viewModel.getRackNumber()!!,
                    viewModel.getPic()!!
                )
                var action = RestockFragmentDirections.actionRestockFragmentToReceiveReceiptFragment(viewModel.getPartNumber()!!, RESTOCK_MODULE_TYPE)
                findNavController().navigate(action)
                viewModel.clear()
            }
        }
        return binding.root
    }

    fun initialStep(){
        if(viewModel.getSerialNumber() != ""){
            var tempMaterials: MutableList<Material>
            tempMaterials = Database.materials

            var partNumber: String = ""
            var quantity: Int = 0
            var isFind: Boolean = false
            var error: Int = 0

            for(tempMaterial in tempMaterials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                for(tempMaterialDetail in tempMaterialDetails){
                    if(tempMaterialDetail.getSerialNumber() == viewModel.getSerialNumber()){
                        if(tempMaterialDetail.getStatus() != 1){
                            error = 1// found, but ady issued or rack
                        }else{
                            partNumber = tempMaterial.getPartNumber()
                            quantity = tempMaterialDetail.getRestQuantity()
                            isFind = true
                        }
                        break
                    }
                }
            }

            if(isFind){
                binding.restockPartNumberEdit.text = partNumber
                binding.serialNumberEdit.text = viewModel.getSerialNumber()
                binding.restockQuantityEdit.text = quantity.toString()
                binding.restockRackIdEdit.text = viewModel.getRackNumber()
                viewModel.setPartNumber(partNumber)
                viewModel.setQuantity(quantity)
            } else {
                if(error == 1){//not exist
                    Toast.makeText(context, "Item Already In Rack / Issued", Toast.LENGTH_SHORT).show()
                    binding.serialNumberEdit.setText("")
                    viewModel.setSerialNumber("")
                } else {
                    Toast.makeText(context, "Serial Number Not Exist, Please Scan Again", Toast.LENGTH_SHORT).show()
                    binding.serialNumberEdit.setText("")
                    viewModel.setSerialNumber("")
                }
            }
        }
    }
}