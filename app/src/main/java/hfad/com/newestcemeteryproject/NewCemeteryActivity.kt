package hfad.com.newestcemeteryproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.databinding.ActivityNewCemeteryBinding
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel
import kotlinx.android.synthetic.main.activity_new_cemetery.*
import java.util.*


class NewCemeteryActivity : AppCompatActivity() {

    private lateinit var viewModel: CemeteryViewModel
    private lateinit var binding: ActivityNewCemeteryBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var  geocoder: Geocoder

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cemetery)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(CemeteryViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()

        geocoder = Geocoder(this, Locale.getDefault())


        binding.locationBtn.setOnClickListener {
            if (!isLocationEnabled()) {
                Toast.makeText(
                    this,
                    "Your location provider is turned off. Please turn it on.",
                    Toast.LENGTH_SHORT
                ).show()

                // This will redirect you to settings from where you need to turn on the location provider.
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else {
                // For Getting current location of user please have a look at below link for better understanding
                // https://www.androdocs.com/kotlin/getting-current-location-latitude-longitude-in-android-using-kotlin.html
                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if (report!!.areAllPermissionsGranted()) {

                                startLocationUpdates()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                    .check()
            }

        }

        binding.addCemButton.setOnClickListener {
            if (binding.nameEditText.text.isNullOrEmpty() ||
                binding.locationEditText.text.isNullOrEmpty() ||
                binding.stateEditText.text.isNullOrEmpty() ||
                binding.countyEditText.text.isNullOrEmpty() ||
                binding.townshipEditText.text.isNullOrEmpty() ||
                binding.rangeEditText.text.isNullOrEmpty() ||
                binding.sectionEditText.text.isNullOrEmpty() ||
                binding.firstYearEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(this, "Please entery all fields", Toast.LENGTH_SHORT).show()
            } else {

                val name = binding.nameEditText.text.toString()
                val location = binding.locationEditText.text.toString()
                val state = binding.stateEditText.text.toString()
                val county = binding.countyEditText.text.toString()
                val townShip = binding.townshipEditText.text.toString()
                val range = binding.rangeEditText.text.toString()
                val section = binding.sectionEditText.text.toString()
                val spot = binding.spotEditText.text.toString()
                val firstYear = binding.firstYearEditText.text.toString()
                val cemetery =
                    Cemetery(
                        cemeteryName = name,
                        cemeteryLocation = location,
                        cemeteryState = state,
                        cemeteryCounty = county,
                        township = townShip,
                        range = range,
                        section = section,
                        spot = spot,
                        firstYear = firstYear
                    )
                viewModel.insertCemetery(cemetery)
                finish()
            }
        }
    }




    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){
                // Update UI with location data
                // ...

                val addressList: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1) //converts lattitude and longitude to an address
                if (addressList != null && addressList.isNotEmpty()) {
                    val address: Address = addressList?.get(0)
                    val sb = StringBuilder()
                    for (i in 0..address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append(",")
                    }
                    sb.deleteCharAt(sb.length - 1) //
                    binding.locationEditText.setText(sb) //set the text to the adress

                }
            }
        }
    }


    @SuppressLint("MissingPermission") //need to put a permission dialog that can take user to the location settings
    private fun startLocationUpdates() {
        fusedLocationClient?.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    private fun isLocationEnabled(): Boolean { //not sure if needed
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }


}





