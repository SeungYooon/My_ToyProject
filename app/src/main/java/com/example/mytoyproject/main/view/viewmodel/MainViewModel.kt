package com.example.mytoyproject.main.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mytoyproject.base.BaseKotlinViewModel
import com.example.mytoyproject.main.view.repository.MainRepository
import com.example.mytoyproject.network.api.RepoService
import com.example.mytoyproject.network.model.GithubItem
import com.example.mytoyproject.network.model.Item
import com.example.mytoyproject.network.model.RepoModel
import com.example.mytoyproject.utils.Resource
import com.example.mytoyproject.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val model: RepoModel) : BaseKotlinViewModel() {

    private val TAG = "MainViewModel"

    private val _repoServiceLiveData = MutableLiveData<GithubItem>()
    val repoServiceLiveData: LiveData<GithubItem>
        get() = _repoServiceLiveData

    fun getRepo(name: String) {
        addDisposable(
            model.getRepo(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        if (totalCount > 0) {
                            Log.d(TAG, "totalcount : $totalCount")
                            _repoServiceLiveData.postValue(this)
                        }
                        Log.d(TAG, "items : $items")
                    }
                }, {
                    Log.d(TAG, "response error, message : ${it.message}")
                })
        )
    }
}
