package com.example.mytoyproject.main.view

import android.content.BroadcastReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.mytoyproject.R
import com.example.mytoyproject.base.BaseKotlinActivity
import com.example.mytoyproject.main.view.adapter.GitAdapter
import com.example.mytoyproject.databinding.ActivityMainBinding
import com.example.mytoyproject.main.view.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModel()
    private val gitAdapter: GitAdapter by inject()

    // if not connected Internet
    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    override fun initStartView() {
        recyclerView.run {
            adapter = gitAdapter
            setHasFixedSize(true)
        }
    }

    override fun initDataBinding() {
        viewModel.repoServiceLiveData.observe(this, Observer {
            it.items.forEach { item ->
                gitAdapter.updateRepo(it.items)
            }
            gitAdapter.notifyDataSetChanged()
        })
    }

    override fun initAfterBinding() {
        imgSearch.setOnClickListener {
            pbShow()
            if (editSearch.text.isEmpty()) {
                pbGone()
                Toast.makeText(this, R.string.emptySearch, Toast.LENGTH_LONG).show()
            } else {
                hideKeyboard()
                if (editSearch.text.toString() != null) {
                    loadRepo()
                } else {
                    loadFail()
                }
            }
        }
    }

    private fun loadRepo() {
        loadSuccess()
        viewModel.getRepo(editSearch.text.toString())
    }

    private fun hideKeyboard() {
        val view = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun loadSuccess() {
        txtNoResponse.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    private fun loadFail() {
        recyclerView.visibility = View.INVISIBLE
        txtNoResponse.visibility = View.VISIBLE
        Toast.makeText(this@MainActivity, "No result for request!", Toast.LENGTH_SHORT).show()
    }

    private fun pbShow() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun pbGone() {
        progressBar.visibility = View.GONE
    }

    private fun connected() {
        recyclerView.visibility = View.VISIBLE
        txtNoConnection.visibility = View.INVISIBLE
    }

    private fun disconnected() {
        recyclerView.visibility = View.INVISIBLE
        txtNoConnection.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
}
