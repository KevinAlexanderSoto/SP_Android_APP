package com.kalex.sp_aplication.di

import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.data.remote.UserRetroApi
import com.kalex.sp_aplication.data.repository.UserRepositoryImpl
import com.kalex.sp_aplication.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSpApi():UserRetroApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserRetroApi ::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api : UserRetroApi):UserRepository{
        return UserRepositoryImpl(api)
    }

}