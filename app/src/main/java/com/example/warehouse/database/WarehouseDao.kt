package com.example.warehouse.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.models.entities.Warehouse

@Dao
abstract class WarehouseDao {
    @Insert
    abstract suspend fun addWarehouse(warehouse: Warehouse)
    @Insert
    abstract suspend fun addMultipleWarehouses(warehouses : List<Warehouse>)
    @Delete
    abstract suspend fun deleteWarehouse(warehouse: Warehouse)
    @Query("SELECT * FROM Warehouse")
    abstract fun getAllWarehouses() : LiveData<List<Warehouse>>
    @Query("SELECT * FROM Warehouse WHERE uuid = :uuid")
    abstract fun getWarehouse(uuid : String) : Warehouse
    @Query("SELECT * FROM Warehouse WHERE name = :name")
    abstract fun getSingleWarehouse(name : String) : Warehouse
    @Query("DELETE FROM Warehouse")
    abstract suspend fun deleteAllWarehouses()
}