package hfad.com.newestcemeteryproject

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.data.Grave
import hfad.com.newestcemeteryproject.databinding.ActivityEditGraveBinding
import hfad.com.newestcemeteryproject.viewmodel.EditGraveActivityViewModel
import java.util.*

class EditGraveActivity : AppCompatActivity() , View.OnClickListener{

    private lateinit var binding: ActivityEditGraveBinding
    private lateinit var viewModel: EditGraveActivityViewModel

    private lateinit var bornDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var deathDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var marriedDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var grave: Grave
    private var graveRowId: Int? = null
    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_grave)
        binding.lifecycleOwner = this

        graveRowId = intent.getIntExtra("grave_row_id", 0)
        viewModel = ViewModelProvider(this).get(EditGraveActivityViewModel::class.java)
        viewModel.getGraveWithId(graveRowId!!)

        bornDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.bornEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        deathDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.deathYearEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        marriedDateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val date: String = "$month / $dayOfMonth / $year"
            binding.marriageYearEt.setText(date) //use this instead of .text since edit text expects exitable instead of string
        }

        binding.bornEt.setOnClickListener(this)
        binding.marriageYearEt.setOnClickListener(this)
        binding.deathYearEt.setOnClickListener(this)


        /*
            If our data binding views are not displaying data we must check if we are setting the data variables in the xml.
             Since we are not using a recycler view we must set this xml's <data> variable here instead of the adapter class.
            We observe view models grave live data, when it changes we set the binding variable in xml and execute the binding methods in binding util file.
         */
        viewModel.grave.observe(this, Observer {
            it?.let {
                binding.grave = it
                grave = it
            }
        })

        binding.createGraveFAB.setOnClickListener {
            val grave =
                Grave(
                    firstName = binding.firstNameEt.text.toString(),
                    lastName = binding.lastNameet.text.toString(),
                    birthDate = binding.bornEt.text.toString(),
                    deathDate = binding.deathYearEt.text.toString(),
                    marriageYear = binding.marriageYearEt.text.toString(),
                    comment = binding.commentEt.text.toString(),
                    cemeteryId = grave.cemeteryId,
                    graveNumber = binding.graveNumEt.text.toString(),
                    id = grave.id
                )
            viewModel.insertGrave(grave)
            finish()
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bornEt -> {
                //cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, bornDateListener, year, month, day)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.deathYearEt -> {
                //cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, deathDateListener, year, month, day)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.marriageYearEt -> {
                //cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, marriedDateListener, year, month, day)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        }
    }


}