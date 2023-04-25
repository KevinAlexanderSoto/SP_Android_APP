package com.kalex.sp_aplication.domain.use_case.get_documentDetail

import com.kalex.sp_aplication.common.Resource
import com.kalex.sp_aplication.data.remote.dto.DocumentDto
import com.kalex.sp_aplication.data.remote.dto.ItemDto
import com.kalex.sp_aplication.domain.repository.UserRepository
import com.kalex.sp_aplication.domain.use_case.post_document.PostDocumentUseCase
import com.kalex.sp_aplication.presentation.validations.states.DocumentDetailState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.onEach
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class GetDocumentDetailUseCaseTest {

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    lateinit var getDocumentDetailUseCase: GetDocumentDetailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getDocumentDetailUseCase = GetDocumentDetailUseCase(userRepository)

    }

    @Test
    fun `when the api return something then get a Resours success`(){
        val listItemDoc = ItemDto(
            "uri",
            "apellido",
            "medellin",
            "correo",
            "fecha",
            "dde",
            "121212",
            "nombre",
            "tipo adj",
            "tipoID"
        )
        val idRegistro = "registro1"
        val document = DocumentDto(Count = 1, Items = listOf<ItemDto>(listItemDoc), ScannedCount = 1)

        coEvery { userRepository.getDocumentDetail(idRegistro) } returns document // co por que es una suspend function

        getDocumentDetailUseCase(idRegistro).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    coVerify() { userRepository.getDocumentDetail(idRegistro) }
                    assertEquals("no esta devolviendo lo que esperaba",document,result.data)
                }
                is Resource.Error -> {
                    assertEquals("no esta devolviendo lo que esperaba",document,result.data)
                }
                is Resource.Loading -> {
                    assertEquals("no esta devolviendo lo que esperaba",document,result.data)
                }
            }
        }


        }

    @After
    fun tearDown() {
    }
    }



