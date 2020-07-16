package com.example.mytoyproject.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseKotlinActivity<T : ViewDataBinding, R : BaseKotlinViewModel> :
    AppCompatActivity() {

    lateinit var viewDatBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initStartView()

    abstract fun initDataBinding()

    abstract fun initAfterBinding()

    private var isSetBackButtonValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDatBinding = DataBindingUtil.setContentView(this, layoutResourceId)

        snackBarObserving()
        initStartView()
        initDataBinding()
        initAfterBinding()
    }

    private fun snackBarObserving() {
        viewModel.observeSnackBarMessage(this) {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.observeSnackBarMessageStr(this) {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
    }
}