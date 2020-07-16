package com.example.mytoyproject.di

import com.example.mytoyproject.main.view.adapter.GitAdapter
import com.example.mytoyproject.main.view.viewmodel.MainViewModel
import com.example.mytoyproject.main.view.viewmodel.UserViewModel
import com.example.mytoyproject.network.api.RepoService
import com.example.mytoyproject.network.api.RetrofitHelper
import com.example.mytoyproject.network.api.UserService
import com.example.mytoyproject.network.model.RepoModel
import com.example.mytoyproject.network.model.RepoModelImpl
import com.example.mytoyproject.network.model.UserModel
import com.example.mytoyproject.network.model.UserModelImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com"

private const val CONNECT_TIMEOUT: Long = 60
private const val READ_TIMEOUT: Long = 60
private const val WRITE_TIMEOUT: Long = 5

private val okHttpClient: OkHttpClient
    get() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

var repoPart = module {
    single<RepoService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RepoService::class.java)
    }
}

var userPart = module {
    single<UserService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}

var adapterPart = module {
    factory {
        GitAdapter()
    }
}

var modelPart = module {
    factory<RepoModel> {
        RepoModelImpl(get())
    }
    factory<UserModel> {
        UserModelImpl(get())
    }
}

var viewModelPart = module {
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        UserViewModel(get())
    }
}


var myDiModule = listOf(repoPart, userPart, adapterPart, modelPart, viewModelPart)