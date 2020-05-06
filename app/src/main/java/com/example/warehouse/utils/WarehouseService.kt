package com.example.warehouse.utils

import com.example.warehouse.models.network.requests.LoginRequest
import com.example.warehouse.models.network.requests.RegisterRequest
import com.example.warehouse.models.network.responses.ItemResponse
import com.example.warehouse.models.network.responses.RegisteredUser
import com.example.warehouse.models.network.responses.WarehouseResponse
import retrofit2.http.*

//TODO Change to interface
interface WarehouseService {
    @POST("login")
    suspend fun login(loginRequest: LoginRequest) : RegisteredUser
    @POST("register")
    suspend fun register(registerRequest: RegisterRequest) : RegisteredUser
    @GET("/inventory/warehouses")
    suspend fun getAllWarehousesForUser(@Header("Authorization") auth : String, @Query("onlyMine") onlyMine : String? = "true") : List<WarehouseResponse>
    @GET("/inventory/warehouses/{warehouse_name}/items")
    suspend fun getAllItemsForUser(@Header("Authorization") auth : String, @Path("warehouse_name") warehouseName : String) : List<ItemResponse>
}