package com.kalex.sp_aplication.data.remote

import com.kalex.sp_aplication.data.remote.dto.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRetroApi {

    @GET("RS_Usuarios")
    suspend fun getUser(
        @Query("idUsuario") idUsuario : String,
        @Query("clave") clave : String
    ): Userdto

    @POST("RS_Documentos")
    suspend fun postDocument(
        @Body requestBody: RequestBody
    ): postDocumentDto

    @GET("RS_Documentos")
    suspend fun getDocuments(
        @Query("correo") correo : String
    ): DocumentDetailDto

    @GET("RS_Documentos")
    suspend fun getDocumentDetail(
        @Query("idRegistro") idRegistro : String,
    ): DocumentDto


    @GET("RS_Oficinas")
    suspend fun getOfices(
        @Query("ciudad") ciudad : String
    ): OficeDto

    @GET("RS_Oficinas")
    suspend fun getAllOfices(
    ): OficeDto

}