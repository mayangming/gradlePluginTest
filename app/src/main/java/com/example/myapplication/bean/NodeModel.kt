package com.example.myapplication.bean

//节点
data class NodeModel(
    val nodeId: Int = 0,//节点id
    val hasParent: Boolean = false, //是否有父节点
    val hasChild: Boolean = false, //是否有子节点
    val nodeCode: Int = 0, //节点编码
    val nodeType: Int = 1, //节点类型
    val nodeName: String = "root", //节点名字
)