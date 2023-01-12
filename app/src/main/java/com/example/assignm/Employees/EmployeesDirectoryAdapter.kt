package com.example.assignm.Employees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignm.Database
import com.example.assignm.R

class EmployeesDirectoryAdapter: RecyclerView.Adapter<EmployeesDirectoryAdapter.ViewHolder>() {

    companion object{
        private lateinit var fragmentActi : EmployeeDirectoryFragment
        fun setFragment(fragmentActi: EmployeeDirectoryFragment){
            this.fragmentActi = fragmentActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesDirectoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_employee, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EmployeesDirectoryAdapter.ViewHolder, position: Int) {
        holder.name.text = Database.employees[position].getName()
        holder.phoneNo.text = Database.employees[position].getTelNo()
        holder.position.text = Database.employees[position].getPosition()
        holder.email.text = Database.employees[position].getEmail()
    }

    override fun getItemCount(): Int {
        return Database.employees.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name: TextView
        var phoneNo: TextView
        var email: TextView
        var position: TextView

        init {
            name = itemView.findViewById(R.id.card_name)
            phoneNo = itemView.findViewById(R.id.card_phone_no)
            email = itemView.findViewById(R.id.card_email)
            position = itemView.findViewById(R.id.card_position)

            itemView.setOnClickListener{
                val position: Int = adapterPosition
                toDetail(fragmentActi,position)
            }
        }
    }

    fun toDetail(fragmentAct: EmployeeDirectoryFragment, index: Int){
        fragmentAct.toDetail(index)
    }

}