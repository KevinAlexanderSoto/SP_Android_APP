package com.kalex.sp_aplication.domain.model

// we want to use this in our code
data class Document(
    val Count: Int,
    val Items: List<Item>,
    val ScannedCount: Int
)
