package com.kalex.sp_aplication.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SingInViewModel(
    private val apirepository : APIRepository
): ViewModel() {

    fun singIn (email : String,password:String){
        apirepository.signIn(email,password)
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingInViewModel::class.java)){
            return SingInViewModel(APIRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}