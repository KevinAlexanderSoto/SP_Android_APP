package com.kalex.sp_aplication.presentation.validations

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class EmailvalidationTest {
    private lateinit var emailvalidation:EmailValidation
    @Before
    fun setUp() {
        emailvalidation = EmailValidation()
    }

    @Test
    fun  `user email validated corect returns error null` (){
        emailvalidation.correo = "correo@correo.com"
        emailvalidation.validate()
        assertEquals("Something when wrong",null,emailvalidation.error)
    }

    @Test
    fun  `user email validated not corect returns error message` (){
        emailvalidation.correo = "correo"
        emailvalidation.validate()
        assertEquals("Something when wrong", "Correo no valido ",emailvalidation.error)
    }

    @Test
    fun  `function valid returns true when the email is correct` (){
        emailvalidation.correo = "correo@correo.com"
        val result = emailvalidation.valid()

        assertTrue("Something when wrong, the function do not return TRUE", result)
    }

    @Test
    fun  `function valid returns false when the email is not correct` (){
        emailvalidation.correo = "correocorreo.com"
        val result = emailvalidation.valid()

        assertFalse("Something when wrong, the function do not return FALSE", result)
    }

    @Test
    fun `when EmailValid is correct return true`(){
        val result = isEmailValid("email@kalexmail.com")
        assertTrue("Test return False , big problem",result)
    }

    @Test
    fun `when EmailValid is wrong return false`(){
        val result = isEmailValid("email@kalexmailcom")
        assertFalse("Test return True , big problem",result)
    }

    @After
    fun tearDown() {
    }
}