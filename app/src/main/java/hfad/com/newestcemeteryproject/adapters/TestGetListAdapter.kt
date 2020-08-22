package hfad.com.newestcemeteryproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.data.Grave
import hfad.com.newestcemeteryproject.databinding.GraveListItemBinding
import hfad.com.newestcemeteryproject.databinding.TestNetworkListItemBinding

class TestGetListAdapter(): ListAdapter<Cemetery, TestGetListAdapter.ViewHolder>(TestDiffUtilCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestNetworkListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        Log.i("GraveListAdapter", "GraveListAdapter onBind called")

    }


    class ViewHolder(val binding: TestNetworkListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Cemetery){
            binding.cemetery = item
            binding.executePendingBindings()
            Log.i("GraveListAdapter", "GraveListAdapter onBind called")

        }
    }
}

class TestDiffUtilCallback: DiffUtil.ItemCallback<Cemetery>(){
    override fun areItemsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem == newItem
    }
}