package com.interview.cisco.thousandeyes.di

import com.interview.cisco.thousandeyes.BuildConfig
import com.interview.cisco.thousandeyes.data.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            callTimeout(40, TimeUnit.SECONDS)
            connectTimeout(40, TimeUnit.SECONDS)
            readTimeout(40, TimeUnit.SECONDS)
            writeTimeout(40, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
        }.build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideRestApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}