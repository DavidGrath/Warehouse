package com.example.warehouse.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.warehouse.database.InventoryItemDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.utils.RetrofitInstance
import com.example.warehouse.utils.WarehouseRepository
import com.example.warehouse.utils.WarehouseService

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val database = InventoryItemDatabase.getDatabase(application)
    private val inventoryItemDao = database.inventoryItemDao()
    private val service = RetrofitInstance.warehouseService
    private val warehouseRepository = WarehouseRepository(inventoryItemDao, service)
    val items: LiveData<List<InventoryItem>>
    init {
        items = warehouseRepository.getAllItems()
    }
}