package com.kalex.sp_aplication.domain.use_case.post_document

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.postDocumentDto
import com.kalex.sp_aplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostDocumentUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(body: RequestBody): Flow<Resource<postDocumentDto>> = flow {
        try {
            emit(Resource.Loading<postDocumentDto>())

            val respuesta = repository.postDocument(body)

            emit(Resource.Success<postDocumentDto>(respuesta))
        } catch (e: HttpException) {
            emit(Resource.Error<postDocumentDto>(e.localizedMessage ?: "an unexpeted error occured"))
        } catch (e: IOException) { // no puede comunicarse sin internet por ejemplo
            emit(Resource.Error<postDocumentDto>("error internet connection"))
        }
    }
}
