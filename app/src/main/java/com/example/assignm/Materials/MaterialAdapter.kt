package com.example.assignm.Materials

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.Database
import com.example.assignm.R

class MaterialAdapter: RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    companion object{
        private lateinit var fragmentActi : MaterialFragment
        fun setFragment(fragmentActi: MaterialFragment){
            this.fragmentActi = fragmentActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_materials, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MaterialAdapter.ViewHolder, position: Int) {
        holder.partNo.text = Database.materials[position].getPartNumber()
        holder.totalQuantity.text = Database.materials[position].getTotalQuantity().toString()
    }

    override fun getItemCount(): Int {
        return Database.materials.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var partNo: TextView
        var totalQuantity: TextView
        init {
            partNo = itemView.findViewById(R.id.card_part_number2)
            totalQuantity = itemView.findViewById(R.id.card_remain_quantity)

            itemView.setOnClickListener{
                val position: Int = adapterPosition
                toDetail(fragmentActi,position)
            }
        }
    }

    fun toDetail(fragmentAct: MaterialFragment, index: Int){
        fragmentAct.toDetail(index)
    }
}