package com.example.myapplication

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

//获取全局的ViewModel存储模式
//代码参考 FragmentManager、FragmentManagerViewModel、ViewModelStore、ViewModelStoreOwner
//Factory参考 ComponentActivity.viewModels第44行、ComponentActivity第573行、SavedStateViewModelFactory第95行
//object ApplicationViewModel {
//    val mViewModelStore = ViewModelStore()//设置一个存储ViewModel的仓库,虽然这个是一个仓库，但是并不能直接存取。需要借助ViewModelProvider
//    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(null)
//    fun <T:ViewModel> getViewModel(clazz: Class<T>){
//        val viewModelProvider = ViewModelProvider(
//            mViewModelStore,
//            factory
//            //后续用拓展函数该
//        )
//        viewModelProvider.get(clazz)
//    }
//
//}
//class UserViewModelFactory(
//    private val type: String
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        modelClass.getConstructor(String::class.java).newInstance(type)
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}



//一个全局的ViewModel
@MainThread
public inline fun <reified VM : ViewModel> ComponentActivity.applicationViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(VM::class, { applicationViewModelStore }, factoryPromise)
}

//val Application.viewModelStore: ViewModelStore by lazy {
//    ViewModelStore()
//}

val applicationViewModelStore: ViewModelStore by lazy {
    ViewModelStore()
}