package com.example.myapplication

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*

class TestAndroidViewModel(private val context: Application): AndroidViewModel(context) {
    val userLiveData = MutableLiveData<String>()
    val userName: LiveData<String> = Transformations.map(userLiveData){
//        MutableLiveData<String>(it)
        "***$it"
    }
    var counter = 0
    fun show(){

        Toast.makeText(context,"llll",Toast.LENGTH_SHORT).show()
    }

    fun add(){
        counter++

    }

    fun changeValue(value: String){
        userLiveData.postValue(value)

    }

}
