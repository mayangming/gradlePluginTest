package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider

class SecondActivity2 : AppCompatActivity() {
    private val viewModel: TestAndroidViewModel by applicationViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second2)
        viewModel.add()
        val result = viewModel.counter
        Log.e("YM-->","结果为:$result")
    }
}