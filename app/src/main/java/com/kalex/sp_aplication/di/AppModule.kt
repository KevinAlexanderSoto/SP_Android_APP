package com.kalex.sp_aplication.di

import android.content.Context
import com.kalex.sp_aplication.SpApplication
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.data.remote.UserRetroApi
import com.kalex.sp_aplication.data.repository.UserRepositoryImpl
import com.kalex.sp_aplication.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSpApi(): UserRetroApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserRetroApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserRetroApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): SpApplication {
        return app as SpApplication
    }
}
