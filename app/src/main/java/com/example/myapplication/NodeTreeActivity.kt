package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bean.NodeModel

class NodeTreeActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.list_item)
    }

    private val nodeAdapter: NodeTreeAdapter by lazy {
        NodeTreeAdapter{
//                val hasChild = it.hasChild
            Log.e("YM--->","节点名字:${it.nodeName}--->是否有子节点:${it.hasChild}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_node_tree)
        initView()
        initData()
    }

    private fun initView(){
        initRecycleView(recyclerView)
    }

    private fun initRecycleView(recyclerView: RecyclerView){
        with(recyclerView){
            layoutManager = LinearLayoutManager(context)
            adapter = nodeAdapter
        }
    }

    private fun initData(){
        nodeAdapter.submitData(createData())
    }

    private fun createData(): List<NodeModel>{
        return arrayListOf(
            NodeModel(
                nodeId = 0,
                nodeCode = 0,
                nodeName = "first item",
                hasChild = true,
                hasParent = false,
            ),
            NodeModel(
                nodeId = 0,
                nodeCode = 0,
                nodeName = "first item",
                hasChild = false,
                hasParent = false,
            ))
    }

}