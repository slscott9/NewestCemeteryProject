package hfad.com.newestcemeteryproject

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.data.Grave
import hfad.com.newestcemeteryproject.databinding.ActivityCreateGraveBinding
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel
import java.util.*

class CreateGraveActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding : ActivityCreateGraveBinding
    private lateinit var bornDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var deathDateListener: DatePickerDialog.OnDateSetListener
    private lateinit var marriedDateListener: DatePickerDialog.OnDateSetListener

    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_grave)
        binding.lifecycleOwner = this
        val cemeteryId = intent.getIntExtra("cemetery_id", 0)
        val viewModel = ViewModelProvider(this).get(CemeteryViewModel::class.java)


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



        binding.createGraveFAB.setOnClickListener {
            if(
                binding.firstNameEt.text.isNullOrEmpty() ||
                binding.lastNameet.text.isNullOrEmpty() ||
                binding.bornEt.text.isNullOrEmpty() ||
                binding.deathYearEt.text.isNullOrEmpty() ||
                binding.marriageYearEt.text.isNullOrEmpty() ||
                binding.commentEt.text.isNullOrEmpty() ||
                binding.graveNumEt.text.isNullOrEmpty()){
                Toast.makeText(this, "Please entery all fields", Toast.LENGTH_SHORT).show()
            }else{
                val grave =
                    Grave(
                        firstName = binding.firstNameEt.text.toString(),
                        lastName = binding.lastNameet.text.toString(),
                        birthDate = binding.bornEt.text.toString(),
                        deathDate = binding.bornEt.text.toString(),
                        marriageYear = binding.marriageYearEt.text.toString(),
                        comment = binding.commentEt.text.toString(),
                        graveNumber = binding.graveNumEt.text.toString(),
                        cemeteryId = cemeteryId)
                Log.i("CreateGraveActivity", "cemetery id passed is $cemeteryId")
                viewModel.insertGrave(grave)
                viewModel.sendGraveToNetwork(grave)
                //viewModel.getGraveList(cemeteryId)
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.bornEt -> {
                cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, bornDateListener, year, month, day)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.deathYearEt -> {
                cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, deathDateListener, year, month, day)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }

            R.id.marriageYearEt -> {
                cal = Calendar.getInstance()
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