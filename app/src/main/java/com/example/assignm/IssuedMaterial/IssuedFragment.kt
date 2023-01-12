package com.example.assignm.IssuedMaterial

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.Material
import com.example.assignm.R
import com.example.assignm.databinding.FragmentIssuedBinding
import java.lang.Integer.parseInt
import java.lang.NumberFormatException

const val ISSUED_MODULE_TYPE = 3

class IssuedFragment : Fragment() {

    private lateinit var binding: FragmentIssuedBinding
    private lateinit var viewModel: IssuedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_issued,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(IssuedViewModel::class.java)

        binding.issuedQuantityEdit.doAfterTextChanged {
            var qty: Int = 0
            try {
                qty = parseInt(binding.issuedQuantityEdit.text.toString())
            }catch (e: NumberFormatException){
                qty = 0
            }
            if(qty > viewModel.getRestQty()!!){
                viewModel.setQuantity(viewModel.getRestQty()!!)
                Toast.makeText(context, "Quantity More Than The Current Stock", Toast.LENGTH_SHORT).show()
                viewModel.setError(true)
            }else if(viewModel.getError() == true){
                viewModel.setError(false)
                viewModel.setQuantity(qty)
            }else{
                viewModel.setQuantity(qty)
            }
        }

        binding.issuedPICEdit.doAfterTextChanged {
            viewModel.setPic(binding.issuedPICEdit.text.toString())
        }

        binding.issuedScanSerialButton.setOnClickListener {
            val action = IssuedFragmentDirections.actionIssuedFragmentToScannerFragment(
                ISSUED_MODULE_TYPE, false)
            findNavController().navigate(action)
        }

        binding.issuedConfirmButton.setOnClickListener {
            if (binding.issuedSerialNumberEdit.text.toString() == ""){
                Toast.makeText(context, "Serial Number Is Empty!", Toast.LENGTH_SHORT).show()
            }else if (parseInt(binding.issuedQuantityEdit.text.toString()) == 0){
                Toast.makeText(context, "Quantity Is Zero!", Toast.LENGTH_SHORT).show()
            }else if (binding.issuedPICEdit.text.toString() == ""){
                Toast.makeText(context, "PIC Is Empty!", Toast.LENGTH_SHORT).show()
            }else if (viewModel.getError() == true){
                Toast.makeText(context, "Quantity More Than The Current Stock", Toast.LENGTH_SHORT).show()
                binding.issuedQuantityEdit.requestFocus()
                // Show the keyboard.
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.issuedQuantityEdit, 0)
            }
            else{
                Database.issueMaterial(
                    viewModel.getPartNumber()!!,
                    viewModel.getSerialNumber()!!,
                    viewModel.getQuantity()!!,
                    binding.issuedPICEdit.text.toString()
                )
                var action = IssuedFragmentDirections.actionIssuedFragmentToReceiveReceiptFragment(viewModel.getPartNumber()!!, ISSUED_MODULE_TYPE)
                findNavController().navigate(action)
                viewModel.clear()
            }
        }

        initialStep()

        return binding.root
    }

    fun initialStep(){
        if(viewModel.getSerialNumber() != ""){
            var tempMaterials: MutableList<Material>
            tempMaterials = Database.materials

            var partNumber: String = ""
            var rackNumber: String = ""
            var isFind: Boolean = false
            var error: Int = 0
            var restQuantity: Int = 0

            for(tempMaterial in tempMaterials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                for(tempMaterialDetail in tempMaterialDetails){
                    if(tempMaterialDetail.getSerialNumber() == viewModel.getSerialNumber()){
                        if(tempMaterialDetail.getStatus() != 2){
                            error = 1// found, but ady issued or rack
                        }else{
                            partNumber = tempMaterial.getPartNumber()
                            rackNumber = tempMaterialDetail.getRackId()
                            restQuantity = tempMaterialDetail.getRestQuantity()
                            isFind = true
                        }
                        break
                    }
                }
            }

            if(isFind){
                binding.issuePartNumberEdit.text = partNumber
                binding.issuedSerialNumberEdit.text = viewModel.getSerialNumber()
                binding.issuedRackIdEdit.text = rackNumber
                binding.issuedPICEdit.setText(viewModel.getPic())
                viewModel.setPartNumber(partNumber)
                viewModel.setRackNumber(rackNumber)
                viewModel.setRestQty(restQuantity)
                viewModel.setQuantity(restQuantity)
                binding.issuedQuantityEdit.setText("10")
                binding.restQuantity.text = viewModel.getRestQty().toString()
                binding.invalidateAll()
            } else {
                if(error == 1){//not exist
                    Toast.makeText(context, "Parts Not In Rack", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Serial Number Not Exist, Please Scan Again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}