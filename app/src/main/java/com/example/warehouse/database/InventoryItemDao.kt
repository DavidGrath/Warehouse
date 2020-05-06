package com.example.warehouse.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.warehouse.models.entities.InventoryItem

@Dao
abstract class InventoryItemDao {
    @Insert
    abstract suspend fun insertItem(inventoryItem: InventoryItem)
    @Update
    abstract suspend fun updateItem(inventoryItem: InventoryItem)
    @Delete
    abstract suspend fun deleteItem(inventoryItem: InventoryItem)
    @Query("SELECT * FROM InventoryItem")
    abstract fun  getAllItems() : LiveData<List<InventoryItem>>
    @Query("DELETE FROM InventoryItem WHERE id = :id")
    abstract suspend fun deleteItemById(id : Long)
}