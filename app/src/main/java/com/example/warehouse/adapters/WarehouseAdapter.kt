package com.example.warehouse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse.R
import com.example.warehouse.models.entities.Warehouse

class WarehouseAdapter(var items : List<Warehouse>) : RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder>(){

    interface ItemClickListener {
        fun onItemClicked(position : Int, warehouse : Warehouse)
    }

    var itemClickListener : ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_warehouse, parent, false)
        return WarehouseViewHolder(v)
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        val warehouse = items[position]
        holder.apply {
            warehouseNameText.text = warehouse.name
            warehouseAddressText.text = warehouse.address
            itemView.setOnClickListener {

                itemClickListener?.onItemClicked(position, warehouse)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class WarehouseViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val warehouseNameText = v.findViewById<TextView>(R.id.warehouse_name_label)
        val warehouseAddressText = v.findViewById<TextView>(R.id.warehouse_address_label)
    }
}