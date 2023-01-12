package com.example.assignm

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignm.databinding.FragmentTransactionReceiptBinding


class TransactionReceiptFragment : Fragment() {
    private lateinit var binding: FragmentTransactionReceiptBinding
    private val args: TransactionReceiptFragmentArgs by navArgs<TransactionReceiptFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_transaction_receipt,container,false)

        if(args.module == 1){
            receiveReceipt()
            activity?.setTitle("Receive Receipt")
        }else if(args.module == 2){
            restockReceipt()
            activity?.setTitle("Restock Receipt")
        }else if(args.module == 3){
            issuedReceipt()
            activity?.setTitle("Issued Receipt")
        }else{
            returnReceipt()
            activity?.setTitle("Return Receipt")
        }

        binding.backButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_receiveReceiptFragment_to_mainMenuFragment)
        }
        binding.issuedBackButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_receiveReceiptFragment_to_mainMenuFragment)
        }
        binding.restockBackButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_receiveReceiptFragment_to_mainMenuFragment)
        }
        binding.returnBackButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_receiveReceiptFragment_to_mainMenuFragment)
        }

        // prevent on back press happen
        val callback = requireActivity().onBackPressedDispatcher.addCallback(activity) {
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun receiveReceipt(){
        //set visibility to visible and other visibility to gone
        binding.receiveReceipt.visibility = View.VISIBLE
        binding.restockReceipt.visibility = View.GONE
        binding.issuedReceipt.visibility = View.GONE
        binding.returnReceipt.visibility = View.GONE

        binding.receiveSerialNumberEdit.text = Database.previousUpdatedMaterialDetail.getSerialNumber()
        binding.receivedQuantityEdit.text = Database.previousUpdatedMaterialDetail.getQuantity().toString()
        binding.receivePICEdit.text = Database.previousUpdatedMaterialDetail.getPic()
        binding.receivedPartNumberEdit.text = args.partNumber
    }

    fun restockReceipt(){
        binding.receiveReceipt.visibility = View.GONE
        binding.restockReceipt.visibility = View.VISIBLE
        binding.issuedReceipt.visibility = View.GONE
        binding.returnReceipt.visibility = View.GONE

        binding.restockSerialNumberEdit.text = Database.previousUpdatedMaterialDetail.getSerialNumber()
        binding.restockPartNumberEdit.text = args.partNumber
        binding.restockQuantityEdit.text = Database.previousUpdatedMaterialDetail.getQuantity().toString()
        binding.restockRackEdit.text = Database.previousUpdatedMaterialDetail.getRackId()
        binding.restockPICEdit.text = Database.previousUpdatedMaterialDetail.getRackPic()
    }

    fun issuedReceipt(){
        binding.receiveReceipt.visibility = View.GONE
        binding.restockReceipt.visibility = View.GONE
        binding.issuedReceipt.visibility = View.VISIBLE
        binding.returnReceipt.visibility = View.GONE

        binding.issuedSerialNumberEdit.text = Database.previousUpdatedMaterialDetail.getSerialNumber()
        binding.issuedPartNumberEdit.text = args.partNumber
        binding.receiptIssuedQuantityEdit.text = (Database.previousUpdatedMaterialDetail.getQuantity() - Database.previousUpdatedMaterialDetail.getRestQuantity() - Database.previousUpdatedMaterialDetail.getReturnQuantity()).toString()
        binding.issuedLeftQuantityEdit.text = Database.previousUpdatedMaterialDetail.getRestQuantity().toString()
        binding.issuedRackEdit.text = Database.previousUpdatedMaterialDetail.getRackId()
        binding.issuedPICEdit.text = Database.previousUpdatedMaterialDetail.getIssuedPic()
    }

    fun returnReceipt(){
        binding.receiveReceipt.visibility = View.GONE
        binding.restockReceipt.visibility = View.GONE
        binding.issuedReceipt.visibility = View.GONE
        binding.returnReceipt.visibility = View.VISIBLE

        binding.returnSerialNumberEdit.text = Database.previousUpdatedMaterialDetail.getSerialNumber()
        binding.returnPartNumberEdit.text = args.partNumber
        binding.receiptReturnQuantityEdit.text = Database.previousUpdatedMaterialDetail.getReturnQuantity().toString()
        binding.returnLeftQuantityEdit.text = Database.previousUpdatedMaterialDetail.getRestQuantity().toString()
        binding.returnPICEdit.text = Database.previousUpdatedMaterialDetail.getReturnPic()
        binding.receiptReturnReasonEdit.text = Database.previousUpdatedMaterialDetail.getReasonReturn()
    }
}