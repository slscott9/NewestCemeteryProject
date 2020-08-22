package hfad.com.newestcemeteryproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hfad.com.newestcemeteryproject.adapters.TestGetListAdapter
import hfad.com.newestcemeteryproject.viewmodel.TestViewModelNetwork
import kotlinx.android.synthetic.main.activity_test_get_list.*

class TestGetListActivity : AppCompatActivity() {

    private lateinit var viewModel : TestViewModelNetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_get_list)

        viewModel = ViewModelProvider(this).get(TestViewModelNetwork::class.java)

        val adapter = TestGetListAdapter()

        viewModel.cemeteryNetworkList.observe(this, Observer {
            adapter.submitList(it)
        })

        testRecyclerView.adapter = adapter



    }
}