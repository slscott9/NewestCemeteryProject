package hfad.com.newestcemeteryproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.data.Grave
import hfad.com.newestcemeteryproject.databinding.ActivityEditGraveBinding
import hfad.com.newestcemeteryproject.viewmodel.EditGraveActivityViewModel

class EditGraveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGraveBinding
    private lateinit var viewModel: EditGraveActivityViewModel
    private lateinit var grave: Grave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_grave)
        binding.lifecycleOwner = this

        val graveRowId = intent.getIntExtra("grave_row_id", 0)
        viewModel = ViewModelProvider(this).get(EditGraveActivityViewModel::class.java)
        viewModel.getGraveWithId(graveRowId)

        /*
            If our data binding views are not displaying data we must check if we are setting the data variables in the xml.
             Since we are not using a recycler view we must set this xml's <data> variable here instead of the adapter class.
            We observe view models grave live data, when it changes we set the binding variable in xml and execute the binding methods in binding util file.
         */
        viewModel.grave.observe(this, Observer {
            it?.let {
                binding.grave = it
                binding.executePendingBindings()
            }
        })

    }
}