package com.example.warehouse.utils

import androidx.lifecycle.LiveData
import com.example.warehouse.database.InventoryItemDao
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.network.requests.LoginRequest
import com.example.warehouse.models.network.requests.RegisterRequest
import com.example.warehouse.models.network.responses.RegisteredUser

class WarehouseRepository(private var inventoryItemDao: InventoryItemDao, private var warehouseService: WarehouseService) {

    lateinit var username : String
    lateinit var token : String

    suspend fun login(loginRequest: LoginRequest) : Result<RegisteredUser?>{
        val result : Result<RegisteredUser?> =
            try {
                Result.Success(warehouseService.login(loginRequest))
            } catch (e : Exception) {
                Result.Failure(null)
            }
        return result
    }

    suspend fun register(registerRequest: RegisterRequest) : Result<RegisteredUser?>{
        val result : Result<RegisteredUser?> =
            try {
                Result.Success(warehouseService.register(registerRequest))
            } catch (e : Exception) {
                Result.Failure(null)
            }
        return result
    }

    fun getAllItems() : LiveData<List<InventoryItem>> {
        return inventoryItemDao.getAllItems()
    }

    suspend fun loadData() {
        val warehouses = warehouseService.getAllWarehousesForUser(token)

    }
}