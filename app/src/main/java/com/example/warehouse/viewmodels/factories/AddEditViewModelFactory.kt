package com.example.warehouse.viewmodels.factories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse.viewmodels.AddEditViewModel

class AddEditViewModelFactory(var app: Application,var warehouseName : String, var warehouseId : String) : ViewModelProvider.AndroidViewModelFactory(app){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddEditViewModel(app, warehouseName, warehouseId) as T
    }

}