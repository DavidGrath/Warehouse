package com.example.warehouse.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.warehouse.database.InventoryItemDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.utils.Result
import com.example.warehouse.utils.RetrofitInstance
import com.example.warehouse.utils.WarehouseRepository
import com.example.warehouse.utils.WarehouseService

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val database = InventoryItemDatabase.getDatabase(application)
    private val inventoryItemDao = database.inventoryItemDao()
    private val warehouseDao = database.warehouseDao()
    private val service = RetrofitInstance.warehouseService
    private val warehouseRepository = WarehouseRepository(inventoryItemDao, warehouseDao, service)
    val warehouses : LiveData<List<Warehouse>>
    lateinit var items: LiveData<List<InventoryItem>>
    var username : String = ""
    set(value) {
        if(field != value) {
            field = value
        }
        warehouseRepository.username = value
    }
    var token : String = ""
    set(value) {
        if(field != value) {
            field = value
        }
        warehouseRepository.token = value
    }
    init {

        warehouseRepository.token = token
        warehouses = warehouseRepository.getAllWarehouses()
    }

    fun setInvItems(warehouseId : String) {
        items = warehouseRepository.getAllItemsFromWarehouse(warehouseId)
    }

    suspend fun deleteItem(uuid : String, warehouseName : String) : Result<Any?> {
        return warehouseRepository.deleteItem(uuid, warehouseName)
    }

    fun getWarehouse(uuid : String) : Warehouse {
        return warehouseRepository.getWarehouse(uuid)
    }
}