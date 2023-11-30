package com.mateusz.onthisday.di

import com.mateusz.onthisday.data.remote.EventApi
import com.mateusz.onthisday.repository.EventRepository
import com.mateusz.onthisday.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideEventRepository(
        api: EventApi
    ) = EventRepository(api)

    @Singleton
    @Provides
    fun provideEventApi(): EventApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(EventApi::class.java)
    }
}