package com.tosta.ukrainiandictionary.model.dto

import com.tosta.ukrainiandictionary.model.entity.Entry
import com.tosta.ukrainiandictionary.utils.slugify

data class EntryDto(
    val slug: String?,
    val ukrainian: String,
    val russian: String,
    val english: String,
) {

    fun toEntity(): Entry {
        return Entry(
            slug = slug ?: ukrainian.slugify(),
            ukrainian = ukrainian,
            russian = russian,
            english = english,
        )
    }
}
