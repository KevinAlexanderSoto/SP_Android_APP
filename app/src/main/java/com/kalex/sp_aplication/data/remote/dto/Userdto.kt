package com.kalex.sp_aplication.data.remote.dto

import com.kalex.sp_aplication.domain.model.User

data class Userdto(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val acceso: Boolean,
    val admin: Boolean = false,
)

fun Userdto.toUser(): User {
    return User(
        id = id,
        nombre = nombre,
        apellido = apellido,
        acceso = acceso,
    )
}
