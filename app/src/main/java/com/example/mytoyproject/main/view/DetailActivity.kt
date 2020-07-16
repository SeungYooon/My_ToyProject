package com.example.mytoyproject.main.view

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.example.mytoyproject.R
import com.example.mytoyproject.base.BaseKotlinActivity
import com.example.mytoyproject.databinding.ActivityDetailBinding
import com.example.mytoyproject.main.view.viewmodel.UserViewModel
import com.example.mytoyproject.utils.GlideApp
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseKotlinActivity<ActivityDetailBinding, UserViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_detail

    override val viewModel: UserViewModel by viewModel()


    companion object {
        const val REPO = "repo"
        const val OWNER = "owner"
        const val AVATAR_URL = "avatar_url"
    }

    private val repo: String by lazy { intent.getStringExtra(REPO) }
    private val owner: String by lazy { intent.getStringExtra(OWNER) }
    private val avatarUrl: String by lazy { intent.getStringExtra(AVATAR_URL) }
    private lateinit var url :String

    override fun initStartView() {
        pbShow()
        if (intent != null) {
            pbGone()
            nameDetail.text = "$owner/"
            repoDetail.text = repo
            GlideApp.with(this).load(avatarUrl).placeholder(R.drawable.ic_search)
                .error(R.mipmap.ic_launcher).override(1024)
                .into(imgDetail)
        }
    }

    override fun initDataBinding() {
        viewModel.ownerServiceLiveData.observe(this, Observer { owner ->
            numStars.text = "${owner.stargazersCount} stars"
            descriptionName.text = owner.description

            if (owner.language == null) {
                languageName.setText(R.string.noLanguage)
            } else {
                loadSuccess()
                languageName.text = owner.language
            }
        })

        viewModel.userServiceLiveData.observe(this, Observer { user ->
            if (followerNum.text == null || followingNum.text == null) {
                loadFail()
            } else {
                loadSuccess()
                followerNum.text = user.followers.toString()
                followingNum.text = user.following.toString()
            }
        })

        viewModel.startEvent.observe(this, Observer {
            url = "$owner/$repo"
            intent = Intent(this, TestActivity::class.java)
            intent.putExtra(TestActivity.URL, url)
            startActivity(intent)
            //startActivity(Intent(applicationContext, TestActivity::class.java))
        })
    }

    override fun initAfterBinding() {
        BackMain.setOnClickListener { finish() }

        viewModel.getOwner(owner, repo)
        viewModel.getUser(owner)

        repoDetail.setOnClickListener {
            viewModel.doSomething()
        }
    }

    private fun pbShow() {
        progressBar.visibility = View.VISIBLE
    }

    private fun pbGone() {
        progressBar.visibility = View.GONE
    }

    private fun loadSuccess() {
        errorPage.visibility = View.GONE
    }

    private fun loadFail() {
        errorPage.visibility = View.VISIBLE
        Toast.makeText(this, "Error Occurred!", Toast.LENGTH_LONG).show()
    }
}
