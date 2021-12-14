package com.kalex.sp_aplication.domain.use_case.get_ofice

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.OficeDto
import com.kalex.sp_aplication.data.remote.dto.toOfice
import com.kalex.sp_aplication.domain.model.Ofice
import com.kalex.sp_aplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOficeUseCase @Inject constructor(
    private val repository: UserRepository // injectamos la interface
){
    operator fun invoke(ciudad: String) : Flow<Resource<OficeDto>> = flow{
        try {
            emit(Resource.Loading<OficeDto>())
            var ofices =  repository.getOfices(ciudad)

            emit(Resource.Success<OficeDto>(ofices))
        }catch (e: HttpException){
            emit(Resource.Error<OficeDto>(e.localizedMessage ?: "an unexpeted error occured"))
        }catch (e: IOException){ // no puede comunicarse sin internet por ejemplo
            emit(Resource.Error<OficeDto>("error internet connection"))
        }
    }
}