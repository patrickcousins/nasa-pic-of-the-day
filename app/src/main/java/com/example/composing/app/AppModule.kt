package com.example.composing.app

import com.example.composing.app.net.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


//    fun provideMoshi() {
//
//    }

    @Provides
    fun provideRetrofit() = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideApiKey() = Config.NASA_API_KEY
}