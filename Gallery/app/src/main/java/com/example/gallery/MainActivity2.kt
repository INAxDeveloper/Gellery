package com.example.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gallery.databinding.ActivityMain2Binding
import com.example.gallery.ui.dashboard.DashboardFragment
import com.example.gallery.ui.home.HomeFragment

class MainActivity2 : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        handlePermissionResult(permissions)
    }

    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navView.setupWithNavController(navController)
        checkPermissionStatus()
    }

    private fun getPermissionsToRequest(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 (API 33) and above
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            // For Android 12 (API 32) and below
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun checkPermissionStatus() {
        val permissions = getPermissionsToRequest()
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
        }
        else {
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = getPermissionsToRequest()

        // Filter for permissions that are not yet granted.
        val permissionsNotGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNotGranted.isNotEmpty()) {
            // Check if we should show a rationale for any of the denied permissions.
            val shouldShowRationale = permissionsNotGranted.any {
                shouldShowRequestPermissionRationale(it)
            }

            if (shouldShowRationale) {
                // Show a dialog explaining why we need the permissions.
                // This is a good user experience practice.
                showPermissionRationaleDialog(permissionsNotGranted.toTypedArray())
            } else {
                // No rationale needed, directly launch the permission dialog.
                requestPermissionLauncher.launch(permissionsNotGranted.toTypedArray())
            }
        } else {
            // All permissions are already granted.
            // Proceed with the action that requires the permissions
            loadAllMedia()
        }
    }

    private fun showPermissionRationaleDialog(permissions: Array<String>) {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("This app needs access to your photos and videos to function properly. Please grant the permissions to continue.")
            .setPositiveButton("OK") { _, _ ->
                // After the user sees the explanation, request the permissions again.
                requestPermissionLauncher.launch(permissions)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // The user chose not to grant the permissions.
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun handlePermissionResult(permissions: Map<String, Boolean>) {
        val allGranted = permissions.all { it.value }

        if (allGranted) {
            loadAllMedia()
        } else {
            // At least one permission was denied.
            val deniedPermissions = permissions.filter { !it.value }.keys
            val permanentlyDenied =
                deniedPermissions.any { !shouldShowRequestPermissionRationale(it) }

            if (permanentlyDenied) {
                // The user selected "Don't ask again" or denied it permanently.
                // We must now guide them to app settings to grant the permission.
                showSettingsDialog()
            } else {
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("You have permanently denied media permissions. Please go to your app settings to grant them manually.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Open app settings to allow the user to grant permissions.
                val intent =
                    android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .apply {
                            val uri = android.net.Uri.fromParts("package", packageName, null)
                            data = uri
                        }
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // A placeholder function for your app's logic after permissions are granted.
    private fun loadAllMedia() {
        Log.d("Permissions", "All media permissions granted. Proceeding to load media...")
        // e.g., navigate to a gallery fragment or start a background task.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main2) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
        when (currentFragment) {
            is DashboardFragment -> currentFragment.loadImages()
            is HomeFragment -> currentFragment.loadVideos()
        }

    }

}
