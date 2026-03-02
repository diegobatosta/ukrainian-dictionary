package com.tosta.ukrainiandictionary.model.dto

import com.tosta.ukrainiandictionary.model.entity.Entry

data class EntryDto(
    val inUkrainian: String?,
    val inRussian: String?,
    val inEnglish: String?,
) {
    fun toEntity(id: Long? = null): Entry {
        return Entry(
            id = id,
            inUkrainian = inUkrainian,
            inRussian = inRussian,
            inEnglish = inEnglish,
        )
    }
}