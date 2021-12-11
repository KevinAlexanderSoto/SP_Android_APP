package com.kalex.sp_aplication.Model
import com.kalex.sp_aplication.data.RsUserProvider
import com.kalex.sp_aplication.data.RsUsuariosAPI
import kotlinx.coroutines.*


class APIRepository {
    private var rsUsuariosAPI : RsUsuariosAPI? = null

    fun signIn(email: String, password: String):Boolean? {
        rsUsuariosAPI = RsUserProvider.newUserApi(email,password)
        var resp:Boolean? =false
         runBlocking (){
             launch {  resp =  rsUsuariosAPI?.getUser(email,password)?.body()?.acceso}
         }
        return resp
    }
}