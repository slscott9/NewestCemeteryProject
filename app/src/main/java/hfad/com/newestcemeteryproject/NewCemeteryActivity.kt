package hfad.com.newestcemeteryproject

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*

import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.databinding.ActivityNewCemeteryBinding
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_cemetery.*
import java.util.*


class NewCemeteryActivity : AppCompatActivity() {

    private lateinit var viewModel: CemeteryViewModel
    private lateinit var binding: ActivityNewCemeteryBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
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


        binding.locationEditText.setOnClickListener {

        }

        binding.addCemButton.setOnClickListener {
            if (binding.nameEditText.text.isNullOrEmpty() ||
                binding.locationEditText.text.isNullOrEmpty() ||
                binding.stateEditText.text.isNullOrEmpty() ||
                binding.countyEditText.text.isNullOrEmpty() ||
                binding.townshipEditText.text.isNullOrEmpty() ||
                binding.rangeEditText.text.isNullOrEmpty() ||
                binding.sectionEditText.text.isNullOrEmpty() ||
                binding.gpsEditText.text.isNullOrEmpty() ||
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
                val gps = binding.gpsEditText.text.toString()
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
                        gps = gps,
                        firstYear = firstYear
                    )
                viewModel.insertCemetery(cemetery)
                finish()
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...

                    val addressList: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1) //converts lattitude and longitude to an address

                    /*
                            USE THE CODE FROM HAPPY PLACES TO CONVERT THE ADDRESS AND TAKE OUT THE EXTRA STUFF IN THE ARRAY
                     */

                    binding.locationEditText.setText(addressList?.get(0).toString()) //set the text to the adress

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()

    }

    private fun isLocationEnabled(): Boolean { //not sure if needed
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission") //need to put a permission dialog that can take user to the location settings
    private fun startLocationUpdates() {
        fusedLocationClient?.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


}





