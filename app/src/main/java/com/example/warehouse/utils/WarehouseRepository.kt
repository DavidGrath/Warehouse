package com.example.warehouse.utils

import androidx.lifecycle.LiveData
import com.example.warehouse.database.InventoryItemDao
import com.example.warehouse.database.WarehouseDao
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.models.network.requests.LoginRequest
import com.example.warehouse.models.network.requests.RegisterRequest
import com.example.warehouse.models.network.responses.ItemResponse
import com.example.warehouse.models.network.responses.RegisteredUser
import com.example.warehouse.models.network.responses.WarehouseResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.ArrayList

class WarehouseRepository(private var inventoryItemDao: InventoryItemDao, private var warehouseDao: WarehouseDao, private var warehouseService: WarehouseService) {

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

    fun getAllItemsNonLive() : List<InventoryItem> {
        return inventoryItemDao.getAllItemsNonLive()
    }
    fun getAllItemsFromWarehouse(warehouseId: String) : LiveData<List<InventoryItem>> {
        return inventoryItemDao.getAllItemsFromWarehouse(warehouseId)
    }


    fun getAllWarehouses() : LiveData<List<Warehouse>> {
        return warehouseDao.getAllWarehouses()
    }

    fun getWarehouse(uuid : String) : Warehouse {
        return warehouseDao.getWarehouse(uuid)
    }
    suspend fun getAllWarehousesForUser(token: String): List<WarehouseResponse> {
        return warehouseService.getAllWarehousesForUser(token)
    }

    suspend fun getAllItemsForUser(token: String, warehouseName: String): LiveData<List<InventoryItem>> {
       return inventoryItemDao.getAllItems()
    }

    suspend fun updateItem(token : String, warehouseId : String, itemId : Long, uuid : String, warehouseName: String, name : String, qty : Int, unitPrice : Float, currCode : String, file : ByteArray, changePicture : Boolean) : Result<Any?>{
        val namePart = MultipartBody.Part.createFormData("name", name)
        val quantityPart = MultipartBody.Part.createFormData("quantity", qty.toString())
        val currCodePart = MultipartBody.Part.createFormData("unitPrice", unitPrice.toString())
        val unitPricePart = MultipartBody.Part.createFormData("currencyCode", currCode)

        val picPart : MultipartBody.Part? = if(changePicture) {MultipartBody.Part.createFormData("picture", "picture",
            file.toRequestBody("image/jpeg".toMediaTypeOrNull()))
        } else null

        try {
            val response = warehouseService.updateItem(token, warehouseName, uuid, changePicture.toString(), namePart, quantityPart, currCodePart, unitPricePart, picPart)
            inventoryItemDao.updateItem(InventoryItem(itemId, warehouseId, response.uuid, response.name, response.quantity, response.currencyCode, response.unitPrice, response.pictureUrl))
            return Result.Success(Any())
        } catch (e : Exception) {
            return Result.Failure(null)
        }
    }

    suspend fun addItem(token : String, warehouseId : String, warehouseName: String, name : String, qty : Int, unitPrice : Float, currCode : String, file : ByteArray) : Result<Any?> {
        val namePart = MultipartBody.Part.createFormData("name", name)
        val quantityPart = MultipartBody.Part.createFormData("quantity", qty.toString())
        val currCodePart = MultipartBody.Part.createFormData("unitPrice", unitPrice.toString())
        val unitPricePart = MultipartBody.Part.createFormData("currencyCode", currCode)

        val picPart = MultipartBody.Part.createFormData("picture", "picture",
            file.toRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        try {
            val response = warehouseService.addItem(token, warehouseName, namePart, quantityPart, currCodePart, unitPricePart, picPart)
            inventoryItemDao.insertItem(InventoryItem(null, warehouseId, response.uuid, response.name, response.quantity, response.currencyCode, response.unitPrice, response.pictureUrl))
            return Result.Success(Any())
        } catch (e : Exception) {
            return Result.Failure(null)
        }
    }

    suspend fun getItem(id : Long) : InventoryItem {
        return inventoryItemDao.getItem(id)
    }

    suspend fun deleteItem(uuid : String, warehouseName: String) : Result<Any?>{
        val result : Result<Any?> = try {
            warehouseService.deleteItem(token, warehouseName, uuid)
            inventoryItemDao.deleteItemByUuid(uuid)
            Result.Success(Any())
        } catch(e : Exception) {
            Result.Failure(null)
        }
        return result
    }

    suspend fun loadData() {
        warehouseDao.deleteAllWarehouses()
        inventoryItemDao.deleteAllItems()
        val warehouses = warehouseService.getAllWarehousesForUser(token)
        val warehouseEntities = ArrayList<Warehouse>()
        val itemEntities = ArrayList<InventoryItem>()
        warehouses.forEach { w ->
            warehouseEntities.add(Warehouse(null, w.name, w.address, w.uuid, w.pictureUrl))
            val items = warehouseService.getAllItemsForUser(token, w.name)
            items.forEach { item ->
                itemEntities.add(
                    InventoryItem(null, w.uuid, item.uuid, item.name, item.quantity, item.currencyCode,
                        item.unitPrice, item.pictureUrl)
                )
            }
        }
        warehouseDao.addMultipleWarehouses(warehouseEntities)
        inventoryItemDao.insertMultipleItems(itemEntities)

    }
}