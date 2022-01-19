package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bean.NodeModel
import com.example.myapplication.databinding.ItemNodeBinding

//一个树形的列表
class NodeTreeAdapter(

    private val itemCallBack: (NodeModel) -> Unit
): RecyclerView.Adapter<NodeTreeAdapter.VH>() {

    private val nodeModeList = mutableListOf<NodeModel>()

    fun submitData(list: List<NodeModel>){
        nodeModeList.clear()
        nodeModeList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        nodeModeList.clear()
        notifyDataSetChanged()
    }

    inner class VH(private val binding: ItemNodeBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(item: NodeModel){
            binding.itemNodeName.text = item.nodeName
            binding.root.setOnClickListener {
                itemCallBack.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = LayoutInflater.from(parent.context)
        val binding = ItemNodeBinding.inflate(context, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(nodeModeList[position])
    }

    override fun getItemCount() = nodeModeList.size
}