package com.kalex.sp_aplication.domain.repository

import com.kalex.sp_aplication.data.remote.dto.*
import okhttp3.RequestBody

interface UserRepository {
    suspend fun getUser(idUsuario: String, clave: String): Userdto

    suspend fun postDocument(body: RequestBody): postDocumentDto

    suspend fun getDocuments(correo: String): DocumentDetailDto

    suspend fun getDocumentDetail(idRegistro: String): DocumentDto

    suspend fun getOfices(ciudad: String): OficeDto

    suspend fun getAllOfices(): OficeDto
}
