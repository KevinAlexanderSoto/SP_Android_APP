package com.kalex.sp_aplication.domain.repository

import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.data.remote.dto.OficeDto
import com.kalex.sp_aplication.data.remote.dto.Userdto
import retrofit2.Response

interface UserRepository {
    suspend fun getUser(idUsuario : String,clave : String):Userdto

    suspend fun getDocuments(idRegistro : Int,correo : String) : DocumentDto

    suspend fun getOfices(ciudad : String) : OficeDto
}