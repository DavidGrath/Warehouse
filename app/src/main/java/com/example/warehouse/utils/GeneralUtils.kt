package com.example.warehouse.utils

import com.example.warehouse.utils.Constants.Companion.IP_ADDRESS

class GeneralUtils {
    companion object {
        fun convertUrlToMobile(url : String) : String {
            return url.replaceFirst("localhost", IP_ADDRESS)
        }
    }
}