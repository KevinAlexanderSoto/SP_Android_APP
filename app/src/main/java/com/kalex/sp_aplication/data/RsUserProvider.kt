package com.kalex.sp_aplication.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RsUserProvider {
    fun newUserApi(email: String, password: String): RsUsuariosAPI? {
        //
        val httpClient = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                val request = builder.build()
                chain.proceed(request)
            }.build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/")
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(RsUsuariosAPI::class.java)
    }
}