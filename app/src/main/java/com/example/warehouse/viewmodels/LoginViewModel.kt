package com.example.warehouse.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.warehouse.bind.LoginForm
import com.example.warehouse.bind.RegisterForm
import com.example.warehouse.database.InventoryItemDatabase
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.models.network.requests.LoginRequest
import com.example.warehouse.models.network.requests.RegisterRequest
import com.example.warehouse.models.network.responses.RegisteredUser
import com.example.warehouse.utils.Result
import com.example.warehouse.utils.RetrofitInstance
import com.example.warehouse.utils.WarehouseRepository
import com.example.warehouse.utils.WarehouseService
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import kotlin.random.Random

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var loginForm : LoginForm = LoginForm("", "")
    var registerForm : RegisterForm = RegisterForm("", "",
        "", "", "", "")
    val database = InventoryItemDatabase.getDatabase(application)
    val inventoryItemDao = database.inventoryItemDao()
    val warehouseDao = database.warehouseDao()
    val service = RetrofitInstance.warehouseService
    private val warehouseRepository = WarehouseRepository(inventoryItemDao, warehouseDao, service)


    suspend fun login() : Result<RegisteredUser?> {
        loginForm.currentlyLoggingIn = true
        val result = warehouseRepository.login(LoginRequest(loginForm.username, loginForm.password))
        loginForm.currentlyLoggingIn = false
        return result
    }
    suspend fun register() : Result<RegisteredUser?> {
        registerForm.currentlyRegistering = true
        //Simulate network call
        delay(1_000L)
        val result = warehouseRepository.register(RegisterRequest(registerForm.username, registerForm.fName,
            registerForm.lName, registerForm.eMail, registerForm.password))
        registerForm.currentlyRegistering = false
        return result
    }

    suspend fun loadData(username : String, token : String) {
        warehouseRepository.username = username
        warehouseRepository.token = token
        warehouseRepository.loadData()
    }
}