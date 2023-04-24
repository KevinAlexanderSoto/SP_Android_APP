package com.kalex.sp_aplication.data.repository

import com.kalex.sp_aplication.data.remote.UserRetroApi
import com.kalex.sp_aplication.data.remote.dto.*
import com.kalex.sp_aplication.domain.repository.UserRepository
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserRetroApi,
) : UserRepository {
    override suspend fun getUser(idUsuario: String, clave: String): Userdto {
        return api.getUser(idUsuario, clave)
    }

    override suspend fun postDocument(body: RequestBody): postDocumentDto {
        return api.postDocument(body)
    }

    override suspend fun getDocuments(correo: String): DocumentDetailDto {
        return api.getDocuments(correo)
    }

    override suspend fun getDocumentDetail(idRegistro: String): DocumentDto {
        return api.getDocumentDetail(idRegistro)
    }

    override suspend fun getOfices(ciudad: String): OficeDto {
        return api.getOfices(ciudad)
    }

    override suspend fun getAllOfices(): OficeDto {
        return api.getAllOfices()
    }
}
