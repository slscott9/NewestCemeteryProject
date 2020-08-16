package hfad.com.newestcemeteryproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.databinding.CemeteryListItemBinding

class CemeteryListAdapter(val clickListener: CemeteryListener): ListAdapter<Cemetery, CemeteryListAdapter.ViewHolder>(CemeteryDiffUtilCallback()){ //1.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CemeteryListItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder (val binding: CemeteryListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: Cemetery,
            clickListener: CemeteryListener
        ){
            binding.cemetery = item
            binding.clickListener = clickListener //14.
            binding.executePendingBindings()
        }
    }
}

class CemeteryDiffUtilCallback: DiffUtil.ItemCallback<Cemetery>(){
    override fun areItemsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Cemetery, newItem: Cemetery): Boolean {
        return oldItem == newItem
    }
}

class CemeteryListener(val clickListener: (id: Int) -> Unit){
    fun onClick(cemetery: Cemetery){
        clickListener(cemetery.id)
    }
}

