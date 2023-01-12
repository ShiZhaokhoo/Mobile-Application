package com.example.assignm.Materials.MaterialDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.Materials.MaterialViewModel
import com.example.assignm.R
import com.example.assignm.databinding.FragmentMaterialDetailBinding

class MaterialDetailFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<MaterialDetailAdapter.ViewHolder>? = null
    private lateinit var binding: FragmentMaterialDetailBinding
    private lateinit var viewModel: MaterialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(MaterialViewModel::class.java)
        MaterialDetailAdapter.setPartIndex(viewModel.getPartIndex()!!, this)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material_detail, container, false)

        binding.recycleViewMaterialDetail.removeAllViewsInLayout()

        layoutManager = LinearLayoutManager(context)

        binding.recycleViewMaterialDetail.layoutManager = layoutManager

        adapter = MaterialDetailAdapter()

        binding.recycleViewMaterialDetail.adapter = adapter



        return binding.root
    }

    fun toFurther(serialIndex: Int){
        viewModel.setSerialIndex(serialIndex)
        findNavController().navigate(R.id.action_materialDetailFragment_to_materialFurtherDetailsFragment)
    }
}