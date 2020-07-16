package com.example.mytoyproject.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.mytoyproject.utils.SnackBarMessage
import com.example.mytoyproject.utils.SnackBarMessageString
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseKotlinViewModel : ViewModel() {

    private val snackbarMessage = SnackBarMessage()
    private val snackbarMessageString = SnackBarMessageString()

    // rxjava observing
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    // if connect finished
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun observeSnackBarMessage(lifecycleOwner: LifecycleOwner, ob: (Int) -> Unit) {
        snackbarMessage.observe(lifecycleOwner, ob)
    }

    fun observeSnackBarMessageStr(lifecycleOwner: LifecycleOwner, ob: (String) -> Unit) {
        snackbarMessageString.observe(lifecycleOwner, ob)
    }

    fun showSnackBar(stringResourceId: Int) {
        snackbarMessage.value = stringResourceId
    }

    fun showSnackBar(str: String) {
        snackbarMessageString.value = str
    }
}