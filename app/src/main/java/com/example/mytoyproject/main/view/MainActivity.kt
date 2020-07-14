package com.example.mytoyproject.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mytoyproject.R
import com.example.mytoyproject.base.ViewModelFactory
import com.example.mytoyproject.main.view.adapter.GitAdapter
import com.example.mytoyproject.databinding.ActivityMainBinding
import com.example.mytoyproject.main.view.viewmodel.MainViewModel
import com.example.mytoyproject.network.api.RepoHelper
import com.example.mytoyproject.network.api.RetrofitHelper
import com.example.mytoyproject.network.model.Item
import com.example.mytoyproject.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gitAdapter: GitAdapter
    private lateinit var mainViewModel: MainViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewModel()

        gitAdapter = GitAdapter(arrayListOf())
        binding.recyclerView.adapter = gitAdapter
        binding.recyclerView.setHasFixedSize(true)

        binding.imgSearch.setOnClickListener {
            loadData()
            hideKeyboard()
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this, ViewModelFactory(RepoHelper(RetrofitHelper.repoService))
        ).get(MainViewModel::class.java)
    }

    private fun loadData() {
        if (binding.editSearch.text.isEmpty()) {
            Toast.makeText(this, R.string.emptySearch, Toast.LENGTH_LONG).show()
        } else {
            mainViewModel.getRepo(editSearch.text.toString()).observe(this, Observer {
                it.let { resources ->
                    when (resources.status) {
                        Status.SUCCESS -> {
                            pbGone()
                            loadSuccess()
                            resources.data?.let { item -> renderList(item.items) }
                        }
                        Status.LOADING -> {
                            pbShow()
                        }
                        Status.ERROR -> {
                            pbGone()

                            it.data?.let { GithubItem ->
                                if (GithubItem.totalCount == 0) {
                                    loadFail(t = Throwable())
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun renderList(repos: ArrayList<Item>) {
        gitAdapter.apply {
            updateRepo(repos)
        }
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
        binding.txtNoResponse.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun loadFail(t: Throwable) {
        binding.recyclerView.visibility = View.INVISIBLE
        binding.txtNoResponse.visibility = View.VISIBLE
        Toast.makeText(this@MainActivity, "No result for request!", Toast.LENGTH_SHORT).show()
    }

    private fun pbShow() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun pbGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun connected() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.txtNoConnection.visibility = View.INVISIBLE
    }

    private fun disconnected() {
        binding.recyclerView.visibility = View.INVISIBLE
        binding.txtNoConnection.visibility = View.VISIBLE
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
