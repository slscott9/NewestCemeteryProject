package hfad.com.newestcemeteryproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.adapters.GraveListAdapter
import hfad.com.newestcemeteryproject.adapters.GraveListListener
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.databinding.ActivityCemeteryDetailBinding
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel

class CemeteryDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CemeteryViewModel
    private lateinit var binding: ActivityCemeteryDetailBinding

    private var cemeteryId: Int? = null
    private lateinit var cemetery: Cemetery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cemetery_detail)
        binding.lifecycleOwner = this

        cemeteryId = intent.getIntExtra("cemetery_id", 0)
        Log.i("CemeteryDetailActivity", "cem id is $cemeteryId")

        viewModel = ViewModelProvider(this).get(CemeteryViewModel::class.java)
        viewModel.getCemetery(cemeteryId!!)

        val adapter = GraveListAdapter(GraveListListener {
            val intent = Intent(this, GraveDetailActivity::class.java)
            intent.putExtra("grave_id", it)
            startActivity(intent)
        })

        viewModel.cemetery.observe(this, Observer {
            cemetery  = it
            binding.cemeteryViewModel = viewModel

        })

        //observe the grave list if it is not null
        viewModel.gravesWithId.observe(this, Observer {
            adapter.submitList(it)
        })

        binding.addChip.setOnClickListener {
            val intent = Intent(this, CreateGraveActivity::class.java)
            intent.putExtra("cemetery_id", cemeteryId!!)
            startActivity(intent)
        }

        binding.locationChip.setOnClickListener {

            val map = "http://maps.google.co.in/maps?q=${cemetery.cemeteryLocation}" //this formats the address for google maps
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
            startActivity(intent)
        }

        binding.graveRecyclerView.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        cemeteryId?.let { viewModel.getGraveList(it) }


    }


}