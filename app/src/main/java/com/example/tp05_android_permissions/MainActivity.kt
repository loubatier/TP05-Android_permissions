package com.example.tp05_android_permissions

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tp05_android_permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogBuilder = AlertDialog.Builder(baseContext)

        initViews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    println("Autorisé")
                    binding.gpsText.text = getString(R.string.gps_valid)
                    startLocalisation()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    println("Pas autorisé")
                    binding.gpsText.text = getString(R.string.gps_error)
                }
            }
        }
    }

    private fun initViews() {
        binding.gpsButton.setOnClickListener(
            View.OnClickListener {

                // ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION)

                requestFineLocalisationPermission()
            }
        )
    }

    private fun requestFineLocalisationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Afficher une popup pour expliquer à l'utilisateur pourquoi on a besoin de son autorisation
            println("J'explique")

            createPermissionAlertDialog("Explication", "C'est important d'accepter les permissions", "Autoriser", "Refuser")
        } else {
            // Demander la permission
            println("Je demande")

            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun startLocalisation() {
        println("J'utilise ma localisation ")
    }

    private fun createPermissionAlertDialog(title: String, message: String, yes: String, no: String) {
        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            .setPositiveButton(
                yes,
                DialogInterface.OnClickListener {
                    dialog, id ->
                    binding.gpsText.text = getString(R.string.gps_validPlus)
                    dialog.cancel()
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            )
            .setNegativeButton(
                no,
                DialogInterface.OnClickListener {
                    dialog, id ->
                    binding.gpsText.text = getString(R.string.gps_errorPlus)
                    dialog.cancel()
                }
            )

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}
