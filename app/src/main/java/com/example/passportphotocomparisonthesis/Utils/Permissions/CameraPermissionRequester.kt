package com.example.passportphotocomparisonthesis.Utils.Permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CameraPermissionRequest(
    private val fragment: Fragment,
    private val requestPermissionLauncher: ActivityResultLauncher<String>) : PermissionRequester {

    override fun isAccessGranted(): Boolean {
        return ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

}