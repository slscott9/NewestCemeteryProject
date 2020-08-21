package hfad.com.newestcemeteryproject

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.data.Grave
import hfad.com.newestcemeteryproject.databinding.ActivityGraveDetailBinding
import hfad.com.newestcemeteryproject.viewmodel.GraveDetailViewModel

class GraveDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGraveDetailBinding
    private lateinit var viewModel: GraveDetailViewModel
    private var rowId: Int? = null
    private lateinit var grave: Grave


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_grave_detail)
        binding.lifecycleOwner = this
        rowId = intent.getIntExtra("grave_id", 0)
        viewModel = ViewModelProvider(this).get(GraveDetailViewModel::class.java)
        viewModel.getGraveWithRowId(rowId!!) //gets the grave that was clicked from the recycler view. This works because the recycler view rows nums match the tables row nums

        viewModel.grave.observe(this, Observer {
            it?.let {
                binding.grave = it    //set the grave variable for the binding to use for its views everytime a change occurs to view model's grave object
                grave = it            //set the grave object so we can use the grave row id to delete from the table
            }
        })

        binding.deleteChip.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Are you sure you want to delete this grave?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    Log.i("GraveId", "Grave property id is ${rowId}")
                    viewModel.deleteGraveWithId(rowId!!)              //have to use the grave row id from the table to delete the right grave
                    finish()
                }
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Delete Grave")
            alert.show()

        }

        binding.sendChip.setOnClickListener {

            val graveInfo = "${grave.firstName} ${grave.lastName} birth year - ${grave.birthDate}, death year - ${grave.deathDate}, marriage year - ${grave.marriageYear}, comment: ${grave.comment}"
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, graveInfo)
                type = "text/plain"
            }

            val chooser: Intent = Intent.createChooser(intent, "Choose an app")
            startActivity(chooser)
        }

        binding.editChip.setOnClickListener {
            val intent = Intent(this, EditGraveActivity::class.java)
            intent.putExtra("grave_row_id", rowId!!)
            startActivity(intent)
        }
    }
}