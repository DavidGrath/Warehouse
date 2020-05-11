package com.example.warehouse.bind

import androidx.databinding.BaseObservable

class AddEditItemForm(var warehouseName : String, var warehouseId : String, var itemId : Long? = null, var uuid : String? = null, var name : String = "", var quantity : String = "0", var unitPrice : String = "0.00",
                      var imageUrl : String? = null) : BaseObservable(){
}