package com.example.passportphotocomparisonthesis.Utils.Permissions


interface PermissionRequester {

    fun isAccessGranted(): Boolean
    fun requestPermission()
}