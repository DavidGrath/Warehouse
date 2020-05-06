package com.example.warehouse.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Warehouse(
    @PrimaryKey(autoGenerate = true)
    val id : Long?,
    val name : String,
    val address : String,
    val uuid : String,
    val pictureUrl : String
) {
}