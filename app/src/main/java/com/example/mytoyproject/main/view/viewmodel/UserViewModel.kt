package com.example.mytoyproject.main.view.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mytoyproject.R
import com.example.mytoyproject.base.BaseKotlinViewModel
import com.example.mytoyproject.main.view.repository.UserRepository
import com.example.mytoyproject.network.api.UserService
import com.example.mytoyproject.network.model.GithubItem
import com.example.mytoyproject.network.model.Item
import com.example.mytoyproject.network.model.User
import com.example.mytoyproject.network.model.UserModel
import com.example.mytoyproject.utils.Resource
import com.example.mytoyproject.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class UserViewModel(private val model: UserModel) : BaseKotlinViewModel() {

    companion object {
        private const val TAG = "UserViewModel"
    }

    private val _ownerServiceLiveData = MutableLiveData<Item>()
    val ownerServiceLiveData: LiveData<Item>
        get() = _ownerServiceLiveData

    private val _userServiceLiveData = MutableLiveData<User>()
    val userServiceLiveData: LiveData<User>
        get() = _userServiceLiveData

    private val _startEvent = SingleLiveEvent<Any>()
    val startEvent: LiveData<Any>
        get() = _startEvent

    fun getOwner(owner: String, repo: String) {
        addDisposable(
            model.getOwner(owner, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        if (owner != null) {
                            Log.d(TAG, "Owner Status : ${it.description}")
                            _ownerServiceLiveData.postValue(this)
                        }
                    }
                }, {
                    Log.d(TAG, "response error, message : ${it.message}")
                })
        )
    }

    fun getUser(username: String) {
        addDisposable(
            model.getUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        if (followers >= 0 && following >= 0) {
                            Log.d(TAG, "User Status : $followers +','+ $following")
                            _userServiceLiveData.postValue(this)
                        }
                    }
                }, {
                    Log.d(TAG, "response error, message : ${it.message}")
                })
        )
    }

    fun doSomething() {
        _startEvent.call()
    }
}

