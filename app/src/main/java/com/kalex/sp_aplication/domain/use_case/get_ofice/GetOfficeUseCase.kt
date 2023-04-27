package com.kalex.sp_aplication.domain.use_case.get_ofice

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.toOfice
import com.kalex.sp_aplication.domain.model.Ofice
import com.kalex.sp_aplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOfficeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(city: String): Flow<Resource<Ofice>> = flow {
        try {
            emit(Resource.Loading<Ofice>())
            val offices = repository.getOfices(city).toOfice()

            emit(Resource.Success<Ofice>(offices))
        } catch (e: HttpException) {
            emit(Resource.Error<Ofice>(e.localizedMessage ?: "an unexpeted error occured"))
        } catch (e: IOException) { // no puede comunicarse sin internet por ejemplo
            emit(Resource.Error<Ofice>("error internet connection"))
        }
    }
}
