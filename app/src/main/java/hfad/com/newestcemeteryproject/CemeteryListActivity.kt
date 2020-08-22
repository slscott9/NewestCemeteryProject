package hfad.com.newestcemeteryproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hfad.com.newestcemeteryproject.adapters.CemeteryListAdapter
import hfad.com.newestcemeteryproject.adapters.CemeteryListener
import hfad.com.newestcemeteryproject.viewmodel.CemeteryViewModel

class CemeteryListActivity : AppCompatActivity() {

    private lateinit var viewModel: CemeteryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cemetery_list)

        val cemlistRecyclerView = findViewById<RecyclerView>(R.id.cemeteryListRecyclerView)
        val adapter = CemeteryListAdapter(CemeteryListener {
                id -> val intent = Intent(this, CemeteryDetailActivity::class.java)
            intent.putExtra("cemetery_id", id)
            startActivity(intent)
        })


        // Get a new or existing ViewModel from the ViewModelProvider.
        viewModel = ViewModelProvider(this).get(CemeteryViewModel::class.java)

        // Add an observer on the LiveData returned by the getAllCems
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        viewModel.allCemeteries.observe(this, Observer { allCemeteries ->
            // When there is a change in the List of Cemeteries send it to the adapter to update the recycler view
            allCemeteries?.let { adapter.submitList(it) }
        })

        cemlistRecyclerView.adapter = adapter
        cemlistRecyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@CemeteryListActivity, NewCemeteryActivity::class.java)
            startActivity(intent)
        }
    }
}