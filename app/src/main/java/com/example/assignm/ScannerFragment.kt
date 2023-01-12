package com.example.assignm

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode


import com.budiyev.android.codescanner.*
import com.example.assignm.IssuedMaterial.IssuedViewModel
import com.example.assignm.ReceivedMaterial.ReceivedViewModel
import com.example.assignm.RestockMaterial.RestockViewModel
import com.example.assignm.ReturnMaterial.ReturnViewModel
import com.example.assignm.databinding.FragmentScannerBinding

private const val CAMERA_REQUEST_CODE = 101


class ScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentScannerBinding

    private val args: ScannerFragmentArgs by navArgs<ScannerFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_scanner,container,false)

        setupPermissions()
        codeScanner()

        return binding.root
    }

    private fun codeScanner(){

        codeScanner = CodeScanner(requireContext(),binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread{
                    //success
                    //it.text result<<<
                    if(args.module == 1){//received
                        var viewModel: ReceivedViewModel
                        viewModel = ViewModelProvider(requireActivity()).get(ReceivedViewModel::class.java)
                        if(args.isPart == true){
                            viewModel.setPartNumber(it.text)
                        } else{
                            viewModel.setQuantity(it.text)
                        }

                    }else if(args.module == 2){//restock
                        var viewModel: RestockViewModel
                        viewModel = ViewModelProvider(requireActivity()).get(RestockViewModel::class.java)
                        if(args.isPart == true){ // isPart means get serialNumber
                            viewModel.setSerialNumber(it.text)
                        } else{
                            viewModel.setRackNumber(it.text)
                        }
                    }else if(args.module == 3){//issued
                        var viewModel: IssuedViewModel
                        viewModel = ViewModelProvider(requireActivity()).get(IssuedViewModel::class.java)
                        viewModel.setSerialNumber(it.text)
                    }else if(args.module == 4){//problem
                        var viewModel: ReturnViewModel
                        viewModel = ViewModelProvider(requireActivity()).get(ReturnViewModel::class.java)
                        viewModel.setSerialNumber(it.text)
                    }
                    findNavController().popBackStack()
                }
            }

            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread{
                    Log.e("Main","Camera initialization error: ${it.message}")
                }
            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission:Int = ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "You need the camera permission for this application!", Toast.LENGTH_SHORT).show()
                }else{
                    //successful
                }
            }
        }
    }
}