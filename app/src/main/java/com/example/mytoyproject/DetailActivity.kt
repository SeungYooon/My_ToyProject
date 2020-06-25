package com.example.mytoyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mytoyproject.databinding.ActivityDetailBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Response
import java.lang.IllegalArgumentException
import javax.security.auth.callback.Callback

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        if (intent != null) {
            binding.nameDetail.setText("$owner/")
            binding.repoDetail.setText(repo)
            Glide.with(this).load(avatarurl).override(1024)
                .into(binding.imgDetail)
        }

        BackMain.setOnClickListener { finish() }

        loadDetail()

        loadFollow()
    }

    private fun loadDetail() {
        pbShow()

        val disposable = RetrofitHelper.getApi().requestOwner(owner, repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
            }

        /*RetrofitHelper.getApi().requestOwner(owner, repo)
            .enqueue(object : retrofit2.Callback<Item> {
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    if (response.isSuccessful) {
                        pbGone()
                        loadSuccess()

                        val body = response.body()
                        if (body != null) {
                            binding.numStars.setText("${body.stargazersCount} stars")
                            binding.descriptionName.setText(body.description)

                            if (body.language == null) {
                                binding.languageName.setText(R.string.noLanguage)
                            } else {
                                binding.languageName.setText(body.language)
                            }
                        }
                    } else {
                        throw IllegalArgumentException("Server Error")
                        loadFail()
                    }
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {
                    Log.v(
                        "GithubAPI::", "Failed API call with call: " + call +
                                " + exception: " + t
                    )
                    loadFail()
                }
            })*/
    }

    private fun loadFollow() {
        val disposable = RetrofitHelper.getApi().requestUser(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
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
            }

        /* RetrofitHelper.getApi().requestUser(owner)
             .enqueue(object : retrofit2.Callback<User> {
                 override fun onResponse(call: Call<User>, response: Response<User>) {
                     if (response.isSuccessful) {
                         loadSuccess()

                         val body = response.body()
                         if (body != null) {
                             binding.followerNum.setText(body.followers.toString())
                             binding.followingNum.setText(body.following.toString())
                         }
                     } else {
                         throw IllegalArgumentException("error")
                         loadFail()
                     }
                 }

                 override fun onFailure(call: Call<User>, t: Throwable) {
                     Log.v(
                         "GithubAPI::", "Failed API call with call: " + call +
                                 " + exception: " + t
                     )
                     loadFail()
                 }
             })*/
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
    }
}
