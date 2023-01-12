package com.example.assignm.Materials.MaterialDetails

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.Database
import com.example.assignm.Material
import com.example.assignm.Materials.MaterialFragment
import com.example.assignm.Materials.MaterialViewModel
import com.example.assignm.R

class MaterialDetailAdapter: RecyclerView.Adapter<MaterialDetailAdapter.ViewHolder>()  {
    private lateinit var viewModel: MaterialViewModel

    companion object{
        private var partIndex: Int = 0
        private lateinit var fragmentActivity : MaterialDetailFragment
        fun setPartIndex(qty: Int, tempActivity: MaterialDetailFragment){
            partIndex = qty
            fragmentActivity = tempActivity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialDetailAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_material_details, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MaterialDetailAdapter.ViewHolder, position: Int) {
        var materialDetail = Database.materials[partIndex].getMaterialDetail(position)
        holder.partNo.text = Database.materials[partIndex].getPartNumber()
        holder.remainQty.text = materialDetail.getRestQuantity().toString()
        holder.serialNo.text = materialDetail.getSerialNumber()
        holder.status.text = materialDetail.getStatus().toString()
    }

    override fun getItemCount(): Int {
        return Database.materials[partIndex].getMaterialDetails().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var partNo: TextView
        var remainQty: TextView
        var serialNo: TextView
        var status: TextView
        init {
            partNo = itemView.findViewById(R.id.card_part_number2)
            remainQty = itemView.findViewById(R.id.card_remain_quantity)
            serialNo = itemView.findViewById(R.id.card_serial_number2)
            status = itemView.findViewById(R.id.card_status)

            itemView.setOnClickListener{
                val position: Int = adapterPosition
                toFurther(position)
            }
        }
    }

    fun toFurther(serialIndex :Int){
        fragmentActivity.toFurther(serialIndex)
    }


}