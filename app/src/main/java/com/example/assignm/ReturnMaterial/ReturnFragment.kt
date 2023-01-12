package com.example.assignm.ReturnMaterial

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.Material
import com.example.assignm.R
import com.example.assignm.databinding.FragmentReturnBinding
import java.lang.NumberFormatException

const val RETURN_MODULE_TYPE = 4

class ReturnFragment : Fragment() {
    private lateinit var binding: FragmentReturnBinding
    private lateinit var viewModel: ReturnViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_return,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(ReturnViewModel::class.java)

        binding.returnQuantityEdit.doAfterTextChanged {
            var qty: Int = 0
            try {
                qty = Integer.parseInt(binding.returnQuantityEdit.text.toString())
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

        binding.returnOtherReason.doAfterTextChanged {
            viewModel.setReasonReturn(binding.returnOtherReason.text.toString())
            Log.d("returnReason",viewModel.getReasonReturn()!!)
        }

        val reasons = arrayOf("Defective","Damaged","Faulty","Others")
        binding.returnReasonSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setReasonReturn(reasons[position])
                binding.returnOtherReason.isEnabled = viewModel.getReasonReturn() == "Others"
                viewModel.setIsOther(viewModel.getReasonReturn() == "Others")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.returnScanSerialButton.setOnClickListener {
            val action = ReturnFragmentDirections.actionReturnFragmentToScannerFragment(
                RETURN_MODULE_TYPE, false)
            findNavController().navigate(action)
        }

        binding.returnConfirmButton.setOnClickListener {
            val qty = binding.returnQuantityEdit.text.toString().toIntOrNull() ?: 0
            if (binding.returnSerialNumberEdit.text == ""){
                Toast.makeText(context, "Serial Number Is Empty!", Toast.LENGTH_SHORT).show()
            }else if(viewModel.getIsOther() == true && binding.returnOtherReason.text.toString() == ""){
                Toast.makeText(context, "Other Reasons Is Empty", Toast.LENGTH_SHORT).show()
                binding.returnOtherReason.requestFocus()
                showKeyboard(binding.returnOtherReason)
            }else if (qty <= 0 ){
                Toast.makeText(context, "Quantity Is Zero!", Toast.LENGTH_SHORT).show()
                binding.returnQuantityEdit.requestFocus()
                showKeyboard(binding.returnQuantityEdit)
            }else if (viewModel.getError() == true){
                Toast.makeText(context, "Quantity More Than The Current Stock", Toast.LENGTH_SHORT).show()
                binding.returnQuantityEdit.requestFocus()
                showKeyboard(binding.returnQuantityEdit)
            }else if (binding.returnPICEdit.text.toString() == ""){
                Toast.makeText(context, "PIC Is Empty!", Toast.LENGTH_SHORT).show()
                binding.returnPICEdit.requestFocus()
                showKeyboard(binding.returnPICEdit)
            }
            else{
                Database.returnMaterial(
                    viewModel.getPartNumber()!!,
                    viewModel.getSerialNumber()!!,
                    viewModel.getQuantity()!!,
                    binding.returnPICEdit.text.toString(),
                    viewModel.getReasonReturn()!!
                )
                var action = ReturnFragmentDirections.actionReturnFragmentToReceiveReceiptFragment(viewModel.getPartNumber()!!, RETURN_MODULE_TYPE)
                findNavController().navigate(action)
                viewModel.clear()
            }
        }

        initialStep()

        return binding.root
    }

    fun showKeyboard(view: View){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun initialStep(){
        if(viewModel.getSerialNumber() != ""){
            var tempMaterials: MutableList<Material>
            tempMaterials = Database.materials

            var partNumber: String = ""
            var isFind: Boolean = false
            var error: Int = 0
            var restQty: Int = 0

            for(tempMaterial in tempMaterials){
                var tempMaterialDetails: MutableList<Material.MaterialDetail> = tempMaterial.getMaterialDetails()
                for(tempMaterialDetail in tempMaterialDetails){
                    if(tempMaterialDetail.getSerialNumber() == viewModel.getSerialNumber()){
                        if(tempMaterialDetail.getStatus() != 1){
                            error = 1// found, but ady issued or rack
                        }else{
                            partNumber = tempMaterial.getPartNumber()
                            restQty = tempMaterialDetail.getRestQuantity()
                            isFind = true
                        }
                        break
                    }
                }
            }

            if(isFind){
                binding.returnPartNumberEdit.text = partNumber
                binding.returnSerialNumberEdit.text = viewModel.getSerialNumber()
                binding.returnPICEdit.setText(viewModel.getPic())
                viewModel.setRestQty(restQty)
                viewModel.setPartNumber(partNumber)
            } else {
                if(error == 1){//not exist
                    Log.w("w", "ItemAdyInRack")
                    Toast.makeText(context, "Item Already In Rack / Issued / Returned", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("w", "SerialNumberMissing")
                    Toast.makeText(context, "Serial Number Not Exist, Please Scan Again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}