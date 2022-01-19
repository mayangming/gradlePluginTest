package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ToolsBarActivity : AppCompatActivity() {
    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools_bar)
        initToolBar()
    }

    private fun initToolBar(){
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_search -> {
                    Toast.makeText(this,"搜索",Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        findViewById<View>(R.id.center_title).setOnClickListener{
            Toast.makeText(this,"标题",Toast.LENGTH_SHORT).show()
        }
    }

}