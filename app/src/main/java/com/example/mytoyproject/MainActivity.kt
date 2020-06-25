package com.example.mytoyproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mytoyproject.R
import com.example.mytoyproject.RateLimitInterceptor
import com.example.mytoyproject.RetrofitHelper
import com.example.mytoyproject.adapter.GitAdapter
import com.example.mytoyproject.databinding.ActivityMainBinding
import com.example.mytoyproject.network.model.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var gitAdapter: GitAdapter = GitAdapter(itemList = ArrayList())

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

    private val rateLimitInterceptor = RateLimitInterceptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.adapter = gitAdapter
        binding.recyclerView.setHasFixedSize(true)

        binding.imgSearch.setOnClickListener {
            loadData()
            hideKeyboard()
        }
    }

    private fun loadData() {
        if (binding.editSearch.text.isEmpty()) {
            Toast.makeText(this, R.string.emptySearch, Toast.LENGTH_LONG).show()
        } else {
            pbShow()

            val compositeDisposable = CompositeDisposable()

            compositeDisposable.add(
                RetrofitHelper.getApi().requestData(editSearch.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError { throwable ->
                    Log.v("doOnError", "doOnError:" + throwable.message)
                    loadFail(throwable)
                    rateLimitInterceptor
                    disconnected()
                }
                .subscribe { GithubItem ->
                    if (GithubItem.totalCount == 0) {
                        loadFail(t = Throwable())
                    } else {
                        pbGone()
                        loadSuccess()
                        gitAdapter.update(GithubItem.items)
                    }
                })

/*             RetrofitHelper.getApi().requestData(editSearch.text.toString())
                 .enqueue(object : Callback<GithubItem> {
                     override fun onResponse(
                         call: Call<GithubItem>,
                         response: Response<GithubItem>
                     ) {
                         if (response.isSuccessful) {

                             pbGone()

                             val body = response.body()

                             if (body != null) {
                                 loadSuccess()
                                 body.let {
                                     setAdapter(it.items)
                                 }

                                 if (body.totalCount == 0) {
                                     Toast.makeText(
                                         this@MainActivity,
                                         "No result for request!",
                                         Toast.LENGTH_SHORT
                                     ).show()
                                     loadFail()
                                 }
                             }
                         } else {
                             throw IllegalArgumentException("Server Error")
                             rateLimitInterceptor
                             disconnected()
                         }
                     }

                     override fun onFailure(call: Call<GithubItem>, t: Throwable) {
                         Log.v(
                             "GithubAPI::",
                             "Failed API call with call: " + call + " + exception: " + t
                         )
                         disconnected()
                     }
                 })*/
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
        Toast.makeText(
            this@MainActivity,
            "No result for request!",
            Toast.LENGTH_SHORT
        ).show()
        pbGone()
    }

    private fun pbShow() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun pbGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setAdapter(itemList: ArrayList<Item>) {
        val mAdapter = GitAdapter(itemList)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.setHasFixedSize(true)
        mAdapter.notifyDataSetChanged()
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
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
}
