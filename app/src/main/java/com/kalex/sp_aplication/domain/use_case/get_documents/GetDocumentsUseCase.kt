package com.kalex.sp_aplication.domain.use_case.get_documents

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.data.remote.dto.toDocument
import com.kalex.sp_aplication.domain.model.Document
import com.kalex.sp_aplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(
    private val repository: UserRepository // injectamos la interface
){
    operator fun invoke(idRegistro:Int,correo :String) : Flow<Resource<DocumentDto>> = flow{
        try {
            emit(Resource.Loading<DocumentDto>())
            var document =  repository.getDocuments(idRegistro,correo)

            emit(Resource.Success<DocumentDto>(document))
        }catch (e: HttpException){
            emit(Resource.Error<DocumentDto>(e.localizedMessage ?: "an unexpeted error occured"))
        }catch (e: IOException){ // no puede comunicarse sin internet por ejemplo
            emit(Resource.Error<DocumentDto>("error internet connection"))
        }
    }
}