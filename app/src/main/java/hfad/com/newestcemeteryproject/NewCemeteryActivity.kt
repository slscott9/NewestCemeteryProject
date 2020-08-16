package hfad.com.newestcemeteryproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.databinding.ActivityNewCemeteryBinding
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel

class NewCemeteryActivity : AppCompatActivity() {

    private lateinit var editWordView: EditText
    private lateinit var viewModel: CemeteryViewModel
    private lateinit var binding: ActivityNewCemeteryBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cemetery)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(CemeteryViewModel::class.java)


        binding.addCemButton.setOnClickListener {
            if (binding.nameEditText.text.isNullOrEmpty() ||
                binding.locationEditText.text.isNullOrEmpty() ||
                binding.stateEditText.text.isNullOrEmpty() ||
                binding.countyEditText.text.isNullOrEmpty() ||
                binding.townshipEditText.text.isNullOrEmpty() ||
                binding.rangeEditText.text.isNullOrEmpty() ||
                binding.sectionEditText.text.isNullOrEmpty() ||
                binding.gpsEditText.text.isNullOrEmpty() ||
                binding.firstYearEditText.text.isNullOrEmpty()) {
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
                    Cemetery(cemeteryName = name,
                        cemeteryLocation = location,
                        cemeteryState = state,
                        cemeteryCounty = county,
                        township = townShip,
                        range = range,
                        section = section,
                        spot = spot,
                        gps = gps,
                        firstYear = firstYear)
                viewModel.insertCemetery(cemetery)
                finish()


            }
        }
    }


}

