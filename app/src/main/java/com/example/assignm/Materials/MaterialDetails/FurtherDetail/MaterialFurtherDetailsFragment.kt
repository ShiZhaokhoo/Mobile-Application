package com.example.assignm.Materials.MaterialDetails.FurtherDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignm.Database
import com.example.assignm.Materials.MaterialViewModel
import com.example.assignm.R
import com.example.assignm.databinding.FragmentMaterialFurtherDetailsBinding

class MaterialFurtherDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMaterialFurtherDetailsBinding
    private lateinit var viewModel: MaterialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_further_details, container,false)
        viewModel = ViewModelProvider(requireActivity()).get(MaterialViewModel::class.java)

        val partIndex = viewModel.getPartIndex()!!
        val serialIndex = viewModel.getSerialIndex()!!

        val tempMaterialDetail = Database.materials[partIndex].getMaterialDetail(serialIndex)

        binding.serialNumberEdit.text = tempMaterialDetail.getSerialNumber()
        binding.partNumberEdit.text = Database.materials[partIndex].getPartNumber()
        binding.issuesQuantityEdit.text = (tempMaterialDetail.getQuantity() - tempMaterialDetail.getReturnQuantity() - tempMaterialDetail.getRestQuantity()).toString()
        binding.rackEdit.text = tempMaterialDetail.getRackId()
        binding.receiveDateEdit.text = tempMaterialDetail.getReceivedDate()
        binding.receivedQuantityEdit.text = tempMaterialDetail.getQuantity().toString()
        binding.receiverEdit.text = tempMaterialDetail.getPic()
        binding.remainingQuantityEdit.text = tempMaterialDetail.getRestQuantity().toString()
        binding.returnQuantityEdit.text = tempMaterialDetail.getReturnQuantity().toString()
        binding.statusEdit.text = tempMaterialDetail.getStatus().toString()
        when(tempMaterialDetail.getStatus()){
            1 -> binding.statusEdit.text = "Received"
            2 -> binding.statusEdit.text = "Rack"
            3 -> binding.statusEdit.text = "Issued"
            4 -> binding.statusEdit.text = "Returned"
        }

        binding.restockConfirmButton.setOnClickListener {
            findNavController().navigate(R.id.action_materialFurtherDetailsFragment_to_mainMenuFragment)
        }

        return binding.root
    }
}