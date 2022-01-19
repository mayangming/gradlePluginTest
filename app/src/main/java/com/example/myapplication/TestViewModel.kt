package com.example.myapplication

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class TestViewModel: ViewModel() {
    var number = 0

    suspend fun reduce(){
        number--
    }

    override fun onCleared() {
        super.onCleared()
    }

}
@ExperimentalContracts
inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}



fun topFunction(){
}