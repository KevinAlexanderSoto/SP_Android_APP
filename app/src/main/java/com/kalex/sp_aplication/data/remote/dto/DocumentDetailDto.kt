package com.kalex.sp_aplication.data.remote.dto

data class DocumentDetailDto(
    val Count: Int,
    val Items: List<ItemDocDto>,
    val ScannedCount: Int
)