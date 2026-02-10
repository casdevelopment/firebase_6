package com.example.esm.network.koin_module

import android.content.Context
import android.util.Log
import androidx.annotation.Keep

import com.example.esm.R
import com.example.esm.network.ApiInterface
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppConstants.BASE_URL
import com.example.esm.utils.AppConstants.FMR_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Keep
val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRetrofitInterface(get()) }
    factory { provideRetrofit(get(), get()) }
}
var baseUrl = ""

fun provideRetrofit(context: Context, okHttpClient: OkHttpClient): Retrofit {
    if (AppConstants.MOBILE_CODE.equals(1148)){
        baseUrl = "https://esmcollege.cyberasol.com/apiesmcollege.cyberasol.com/api/Mobile/"
    } else if (context.packageName.equals("com.fmr.esm")){
        Log.v("FMR_URL","FMR_URL "+FMR_URL )
        baseUrl = FMR_URL
    } else {
        baseUrl = context.getString(R.string.base_url)
    }
    Log.v("b_url", baseUrl)

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    return OkHttpClient.Builder()
        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
//            request.addHeader("Authorization",utils. AppConstants.AUTH_TOKEN)
//            request.url(AppConstants.BASE_URL)
//                .post()
            // request.addHeader("x-api-key", AppConstants.API_KEY)
            request.addHeader("authorization", "")
            request.addHeader("Accept", "application/json")
            request.addHeader("Content-Type", "application/json")
            chain.proceed(request.build())
        }
        .connectTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .cache(null)
        .build()
}

fun provideRetrofitInterface(retrofit: Retrofit): ApiInterface =
    retrofit.create(ApiInterface::class.java)


