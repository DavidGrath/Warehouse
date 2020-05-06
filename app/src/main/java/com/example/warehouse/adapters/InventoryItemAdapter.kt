package com.example.warehouse.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.warehouse.R
import com.example.warehouse.models.entities.InventoryItem
import java.text.DecimalFormat

class InventoryItemAdapter() : ListAdapter<InventoryItem, InventoryItemAdapter.InventoryItemViewHolder>(DIFF_CALLBACK){

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<InventoryItem>() {
            override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
                return oldItem.id!!.equals(newItem.id!!)
            }

            override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
                return true
            }
        }
    }

    interface PopupItemListener {
        fun onPopupItemClicked(inventoryItem: InventoryItem, menuItemId : Int, position: Int)
    }

    var popupItemListener : PopupItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_inventory_item, parent, false)
        return InventoryItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: InventoryItemViewHolder, position: Int) {
        val inventoryItem = getItem(position)
        holder.apply {
            itemName.text = inventoryItem.name
            if(inventoryItem.imageUri == null) {
                itemPicture.setImageResource(R.drawable.ic_image_not_found)
            } else {
                Glide.with(itemView.context)
                    .load(Uri.parse(inventoryItem.imageUri))
                    .placeholder(R.drawable.ic_image_not_found)
                    .error(R.drawable.ic_image_not_found)
                    .into(itemPicture)
            }
            val format = DecimalFormat("0.00")
            val price = format.format(inventoryItem.unitPrice)
            itemPrice.text = "${inventoryItem.currencyCode} $price"
            itemQuantity.text = inventoryItem.quantity.toString()
            popupButton.setOnClickListener{
                val popup = PopupMenu(itemView.context, it)
                val inflater = popup.menuInflater
                inflater.inflate(R.menu.menu_inventory_item_popup, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    popupItemListener?.onPopupItemClicked(inventoryItem, item!!.itemId, position)
                    true
                }
                popup.show()
            }
        }
    }


    class InventoryItemViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        val itemName = item.findViewById<TextView>(R.id.label_item_name)
        val itemPrice = item.findViewById<TextView>(R.id.label_item_price)
        val itemQuantity = item.findViewById<TextView>(R.id.label_item_qty)
        val itemPicture = item.findViewById<ImageView>(R.id.imageview_inventory_item)
        val popupButton = item.findViewById<ImageView>(R.id.button_inventory_item_popup)
    }
}