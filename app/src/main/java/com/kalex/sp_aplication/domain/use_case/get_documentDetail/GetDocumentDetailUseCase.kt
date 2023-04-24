package com.kalex.sp_aplication.domain.use_case.get_documentDetail

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDocumentDetailUseCase @Inject constructor(
    private val repository: UserRepository, // injectamos la interface
) {
    operator fun invoke(idRegistro: String): Flow<Resource<DocumentDto>> = flow {
        try {
            emit(Resource.Loading<DocumentDto>())

            var document = repository.getDocumentDetail(idRegistro)

            emit(Resource.Success<DocumentDto>(document))
        } catch (e: HttpException) {
            emit(Resource.Error<DocumentDto>(e.localizedMessage ?: "an unexpeted error occured"))
        } catch (e: IOException) { // no puede comunicarse sin internet por ejemplo
            emit(Resource.Error<DocumentDto>("error internet connection"))
        }
    }
}
