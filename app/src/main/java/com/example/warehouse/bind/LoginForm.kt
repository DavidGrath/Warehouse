package com.example.warehouse.bind

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class LoginForm(var username : String, var password : String, currentlyLoggingIn : Boolean = false) : BaseObservable() {
    @Bindable
    var currentlyLoggingIn = currentlyLoggingIn
    set(value) {
        if(field != value) {
            field = value
        }
        notifyPropertyChanged(BR.currentlyLoggingIn)
    }
}