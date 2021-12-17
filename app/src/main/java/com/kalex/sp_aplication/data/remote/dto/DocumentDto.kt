package com.kalex.sp_aplication.data.remote.dto

import com.kalex.sp_aplication.domain.model.Document

data class DocumentDto(
    val Count: Int,
    val Items: List<ItemDto>,
    val ScannedCount: Int
)

fun DocumentDto.toDocument(): Document {
    return Document(
        Items =  Items.map { it.toItem() },

    )
}