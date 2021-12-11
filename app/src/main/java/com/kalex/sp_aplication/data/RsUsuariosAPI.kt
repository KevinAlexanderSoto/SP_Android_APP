package com.kalex.sp_aplication.data


import com.squareup.moshi.JsonClass

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RsUsuariosAPI {
    @GET("RS_Usuarios")
     suspend fun getUser(
        @Query("idUsuario") idUsuario : String,
        @Query("clave") clave : String
    ): Response<Resp>
}

@JsonClass(generateAdapter = true)
data class Resp(
    val id: String ="",
    val nombre: String ="",
    val apellido: String ="",
    val acceso: Boolean,
    val admin: Boolean = false
)