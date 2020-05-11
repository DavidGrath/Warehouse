package com.example.warehouse.utils

import com.example.warehouse.models.network.requests.LoginRequest
import com.example.warehouse.models.network.requests.RegisterRequest
import com.example.warehouse.models.network.responses.ItemResponse
import com.example.warehouse.models.network.responses.RegisteredUser
import com.example.warehouse.models.network.responses.WarehouseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

//TODO Change to interface
interface WarehouseService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : RegisteredUser
    @Multipart
    @POST("register")
    suspend fun register(registerRequest: RegisterRequest) : RegisteredUser
    @Multipart
    @POST("inventory/warehouses/{warehouse_name}/items")
    suspend fun addItem(@Header("Authorization") auth : String,
                        @Path("warehouse_name") warehouseName : String,
                        @Part itemName : MultipartBody.Part,
                        @Part itemQuantity : MultipartBody.Part,
                        @Part itemCurrCode : MultipartBody.Part,
                        @Part itemUnitPrice : MultipartBody.Part,
                        @Part itemPic : MultipartBody.Part) : ItemResponse
    @Multipart
    @PUT("inventory/warehouses/{warehouse_name}/items/{item_id}")
    suspend fun updateItem(@Header("Authorization") auth : String,
                           @Path("warehouse_name") warehouseName : String,
                           @Path("item_id") itemId : String,
                           @Query("change_picture") changePicture : String,
                           @Part itemName : MultipartBody.Part,
                           @Part itemQuantity : MultipartBody.Part,
                           @Part itemCurrCode : MultipartBody.Part,
                           @Part itemUnitPrice : MultipartBody.Part,
                           @Part itemPic : MultipartBody.Part?) : ItemResponse
    @DELETE("inventory/warehouses/{warehouse_name}/items/{item_id}")
    suspend fun deleteItem(@Header("Authorization") auth : String,
                           @Path("warehouse_name") warehouseName : String,
                           @Path("item_id") itemId : String) : Response<Unit>
    @GET("inventory/warehouses")
    suspend fun getAllWarehousesForUser(@Header("Authorization") auth : String, @Query("onlyMine") onlyMine : String? = "true") : List<WarehouseResponse>
    @GET("inventory/warehouses/{warehouse_name}/items")
    suspend fun getAllItemsForUser(@Header("Authorization") auth : String, @Path("warehouse_name") warehouseName : String) : List<ItemResponse>
}