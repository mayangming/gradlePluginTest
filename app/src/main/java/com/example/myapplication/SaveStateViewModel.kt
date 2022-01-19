package com.example.myapplication

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.io.File

//保存状态
//异常保存状态
private fun File.saveTempFile() = bundleOf("path" to absolutePath)
class SaveStateViewModel(private val state: SavedStateHandle): ViewModel() {
    val KEY = "k"
    fun putValue(value: String){
        state.set(KEY,value)
    }

    fun getValue() = state.getLiveData<String>(KEY)
    private var tempFile: File? = null
    init {
        state.setSavedStateProvider("temp_file") { // saveState()
            tempFile?.saveTempFile() ?: Bundle()
        }
    }

    fun createOrGetTempFile(): File {
        return tempFile ?: File.createTempFile("temp", null).also {
            tempFile = it
        }
    }
}