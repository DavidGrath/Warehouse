package com.example.warehouse.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.warehouse.models.entities.InventoryItem

@Dao
abstract class InventoryItemDao {
    @Insert
    abstract suspend fun insertItem(inventoryItem: InventoryItem)
    @Insert
    abstract suspend fun insertMultipleItems(inventoryItems: List<InventoryItem>)
    @Update
    abstract suspend fun updateItem(inventoryItem: InventoryItem)
    @Delete
    abstract suspend fun deleteItem(inventoryItem: InventoryItem)
    @Query("SELECT * FROM InventoryItem")
    abstract fun  getAllItems() : LiveData<List<InventoryItem>>
    @Query("SELECT * FROM InventoryItem")
    abstract fun  getAllItemsNonLive() : List<InventoryItem>
    @Query("SELECT * FROM InventoryItem WHERE warehouseId = :warehouseId")
    abstract fun  getAllItemsFromWarehouse(warehouseId: String) : LiveData<List<InventoryItem>>
    @Query("SELECT * FROM InventoryItem WHERE id = :id")
    abstract suspend fun getItem(id : Long) : InventoryItem
    @Query("DELETE FROM InventoryItem WHERE id = :id")
    abstract suspend fun deleteItemById(id : Long)
    @Query("DELETE FROM InventoryItem WHERE uuid = :uuid")
    abstract suspend fun deleteItemByUuid(uuid : String)
    @Query("DELETE FROM InventoryItem")
    abstract suspend fun deleteAllItems()
    @Query("DELETE FROM InventoryItem WHERE warehouseId = :warehouseId")
    abstract suspend fun deleteAllItemsFromWarehouse(warehouseId : String)

}