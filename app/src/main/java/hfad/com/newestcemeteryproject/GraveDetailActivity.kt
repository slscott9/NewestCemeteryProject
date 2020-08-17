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
//    private lateinit var grave: Grave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = supportActionBar

        binding = DataBindingUtil.setContentView(this, R.layout.activity_grave_detail)

        binding.lifecycleOwner = this
        rowId = intent.getIntExtra("grave_id", 0)
        Log.i("GraveId", "Grave number clicked from recycler view is $rowId")
        viewModel = ViewModelProvider(this).get(GraveDetailViewModel::class.java)

        viewModel.getGraveWithRowId(rowId!!) //gets the grave that was clicked from the recycler view. This works because the recycler view rows nums match the tables row nums

        viewModel.grave.observe(this, Observer {
            it?.let {
                binding.grave = it    //set the grave variable for the binding to use for its views
                //grave = it            //set the grave object so we can use the grave row id to delete from the table
                binding.executePendingBindings()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.edit_menu, menu)
        return  super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_grave_action -> {
                val intent = Intent(this, EditGraveActivity::class.java)
                intent.putExtra("grave_row_id", rowId)
                startActivity(intent)
            }

            R.id.delete_grave_action -> {
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
        }
        return super.onOptionsItemSelected(item)
    }
}