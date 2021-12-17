package com.kalex.sp_aplication.data.remote

import com.kalex.sp_aplication.data.remote.dto.DocumentDetailDto
import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.data.remote.dto.OficeDto
import com.kalex.sp_aplication.data.remote.dto.Userdto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserRetroApi {

    @GET("RS_Usuarios")
    suspend fun getUser(
        @Query("idUsuario") idUsuario : String,
        @Query("clave") clave : String
    ): Userdto

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