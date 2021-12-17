package com.kalex.sp_aplication.data.remote.dto

import com.kalex.sp_aplication.domain.model.Ofice

data class OficeDto(
    val Count: Int,
    val Items: List<ItemOficeDto>,
    val ScannedCount: Int
)

fun OficeDto.toOfice(): Ofice {
    return Ofice(
     Items= Items.map { it.toItemOfice() }
    )
}