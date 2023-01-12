package com.giussepr.zemoga.core.di

import com.giussepr.zemoga.data.api.PlaceholderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {

  @Provides
  fun provideBaseUrl(): String = "https://jsonplaceholder.typicode.com/"

  @Provides
  fun provideLoginInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides
  fun provideRetrofit(baseUrl: String, loggingInterceptor: HttpLoggingInterceptor): PlaceholderApi {

    val client = OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .build()

    return retrofit.create(PlaceholderApi::class.java)
  }
}
