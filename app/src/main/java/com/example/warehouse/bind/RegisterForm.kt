package com.example.warehouse.bind

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class RegisterForm(var username : String, var fName : String, var lName : String,
                   var eMail : String, var password : String, var confirmPassword : String,
                   currentlyRegistering : Boolean = false) : BaseObservable() {
    @Bindable
    var currentlyRegistering = currentlyRegistering
    set(value) {
        if(field != value) {
            field = value
        }
//        notifyPropertyChanged()
    }
}