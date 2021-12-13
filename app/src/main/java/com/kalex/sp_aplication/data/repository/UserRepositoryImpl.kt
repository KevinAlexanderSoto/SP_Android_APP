package com.kalex.sp_aplication.data.repository

import com.kalex.sp_aplication.data.remote.UserRetroApi
import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.data.remote.dto.OficeDto
import com.kalex.sp_aplication.data.remote.dto.Userdto
import com.kalex.sp_aplication.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api : UserRetroApi
):UserRepository {
    override suspend fun getUser(idUsuario: String, clave: String): Userdto{
        return api.getUser(idUsuario, clave)
    }

    override suspend fun getDocuments(idRegistro: Int, correo: String): DocumentDto {
        return api.getDocuments(idRegistro ,correo)
    }

    override suspend fun getOfice(ciudad: String): OficeDto {
       return api.getOfices(ciudad)
    }
}