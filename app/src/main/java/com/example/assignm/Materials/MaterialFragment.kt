package com.example.assignm.Materials

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.Database
import com.example.assignm.Materials.MaterialDetails.MaterialDetailAdapter
import com.example.assignm.R
import com.example.assignm.databinding.FragmentMaterialBinding

class MaterialFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MaterialAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentMaterialBinding
    private lateinit var viewModel: MaterialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MaterialViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)

        layoutManager = LinearLayoutManager(context)

        binding.recycleViewMaterials.layoutManager = layoutManager

        MaterialAdapter.setFragment(this)

        adapter = MaterialAdapter()
        binding.recycleViewMaterials.adapter = adapter

        return binding.root
    }

    fun toDetail(index: Int){
        viewModel.setPartIndex(index)
        findNavController().navigate(R.id.action_materialFragment_to_materialDetailFragment)
    }
}