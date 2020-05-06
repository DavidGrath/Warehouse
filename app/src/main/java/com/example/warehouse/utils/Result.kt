package com.example.warehouse.utils

sealed class Result<T>(var data : T? = null) {
    class Success<T>(data : T) : Result<T>(data)
    class Failure<T>(data : T) : Result<T>(data)
    class Error<T>(data : T, throwable: Throwable? = null) : Result<T>(data)
}