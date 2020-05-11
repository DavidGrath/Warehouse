package com.example.warehouse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse.R

class EmptyAdapter(val title : String, val desc : String) : RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        return EmptyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_empty, parent, false))
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        holder.apply {
            emptyText.text = title
            emptyDesc.text = desc
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class EmptyViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val emptyText = v.findViewById<TextView>(R.id.recyclerview_empty_label)
        val emptyDesc = v.findViewById<TextView>(R.id.recylerview_empty_desc)
    }
}