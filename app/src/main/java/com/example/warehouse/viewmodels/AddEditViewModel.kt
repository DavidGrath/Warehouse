package com.example.warehouse.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.warehouse.bind.AddEditItemForm
import com.example.warehouse.database.InventoryItemDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.utils.Result
import com.example.warehouse.utils.RetrofitInstance
import com.example.warehouse.utils.WarehouseRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddEditViewModel(application: Application, var warehouseName: String, var warehouseId : String) : AndroidViewModel(application){
    var addEditForm : AddEditItemForm = AddEditItemForm(warehouseName, warehouseId)
    val database = InventoryItemDatabase.getDatabase(application)
    val repo = WarehouseRepository(database.inventoryItemDao(), database.warehouseDao(), RetrofitInstance.warehouseService)

    suspend fun getItem(itemId : Long) : InventoryItem {
        return repo.getItem(itemId)
    }

    suspend fun addItem(token : String, currCode : String, file : ByteArray) : Result<Any?> {
        return repo.addItem(token, addEditForm.warehouseId, addEditForm.warehouseName, addEditForm.name,
            addEditForm.quantity.toInt(), addEditForm.unitPrice.toFloat(), currCode, file)
    }

    suspend fun updateItem(token : String, currCode : String, file : ByteArray, changePicture : Boolean) : Result<Any?> {
        return repo.updateItem(token, addEditForm.warehouseId, addEditForm.itemId!!, addEditForm.uuid!!, addEditForm.warehouseName, addEditForm.name,
            addEditForm.quantity.toInt(), addEditForm.unitPrice.toFloat(), currCode, file, changePicture)
    }

    fun getAllItemsNonLive() : List<InventoryItem> {
        return repo.getAllItemsNonLive()
    }

}