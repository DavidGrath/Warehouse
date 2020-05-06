package com.example.warehouse.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null,
    var name : String,
    var quantity : Int,
    var currencyCode : String,
    var unitPrice : Float,
    var imageUri : String? = null) {
}