package com.example.mytoyproject.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.mytoyproject.R
import com.example.mytoyproject.base.UserModelFactory
import com.example.mytoyproject.base.ViewModelFactory
import com.example.mytoyproject.databinding.ActivityDetailBinding
import com.example.mytoyproject.main.view.viewmodel.MainViewModel
import com.example.mytoyproject.main.view.viewmodel.UserViewModel
import com.example.mytoyproject.network.api.*
import com.example.mytoyproject.network.model.User
import com.example.mytoyproject.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userViewModel: UserViewModel

    companion object {
        const val REPO = "repo"
        const val OWNER = "owner"
        const val AVATAR_URL = "avatar_url"
    }

    private val repo: String by lazy { intent.getStringExtra(REPO) }
    private val owner: String by lazy { intent.getStringExtra(OWNER) }
    private val avatarurl: String by lazy { intent.getStringExtra(AVATAR_URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_detail
        )

        if (intent != null) {
            binding.nameDetail.setText("$owner/")
            binding.repoDetail.setText(repo)
            Glide.with(this).load(avatarurl).override(1024)
                .into(binding.imgDetail)
        }

        BackMain.setOnClickListener { finish() }

        setupViewModel()
        loadDetail()
        loadFollow()

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this, ViewModelFactory(RepoHelper(RetrofitHelper.repoService))
        ).get(MainViewModel::class.java)

        userViewModel = ViewModelProviders.of(
            this, UserModelFactory(UserHelper(RetrofitHelper.userService))
        ).get(UserViewModel::class.java)
    }

    private fun loadDetail() {
        pbShow()
        mainViewModel.getOwner(owner, repo).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    pbGone()
                    it.data?.let { owner ->
                        if (owner != null) {
                            loadSuccess()
                            binding.numStars.setText("${owner.description} stars")
                            binding.descriptionName.setText(owner.description)
                        }
                        if (owner.language == null) {
                            binding.languageName.setText(R.string.noLanguage)
                        } else {
                            binding.languageName.setText(owner.language)
                        }
                    }
                }
                Status.LOADING -> {
                    pbShow()
                }
                Status.ERROR -> {
                    loadFail(t = Throwable())
                }
            }
        })


        /* val disposable = RetrofitHelper.getRepoApi().getOwner(owner, repo)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnError { throwable ->
                 Log.v("doOnError", "doOnError:" + throwable.message)
                 loadFail(throwable)
             }
             .subscribe { GithubItem ->
                 if (GithubItem != null) {
                     pbGone()
                     loadSuccess()
                     binding.numStars.setText("${GithubItem.stargazersCount} stars")
                     binding.descriptionName.setText(GithubItem.description)
                 }
                 if (GithubItem.language == null) {
                     binding.languageName.setText(R.string.noLanguage)
                 } else {
                     binding.languageName.setText(GithubItem.language)
                 }
             }*/
    }

    private fun loadFollow() {
        userViewModel.getUser(owner).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    pbGone()
                    it.data?.let { user ->
                        if (user != null) {
                            loadSuccess()
                            binding.followerNum.setText(user.followers.toString())
                            binding.followingNum.setText(user.following.toString())
                        }
                    }
                }
                Status.LOADING -> {
                    pbShow()
                }
                Status.ERROR -> {
                    loadFail(t = Throwable())
                }
            }
        })
        /* val disposable = RetrofitHelper.getUserApi().getUser(owner)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnError { throwable ->
                 Log.v("doOnError", "doOnError:" + throwable.message)
                 loadFail(throwable)
             }
             .subscribe { User ->
                 if (User != null) {
                     pbGone()
                     loadSuccess()
                     binding.followerNum.setText(User.followers.toString())
                     binding.followingNum.setText(User.following.toString())
                 }
             }*/
    }

    private fun pbShow() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun pbGone() {
        binding.progressBar.visibility = View.GONE
    }

    private fun loadSuccess() {
        binding.errorPage.visibility = View.GONE
    }

    private fun loadFail(t: Throwable) {
        binding.errorPage.visibility = View.VISIBLE
        Toast.makeText(this, "Error Occured!", Toast.LENGTH_LONG).show()
    }
}
