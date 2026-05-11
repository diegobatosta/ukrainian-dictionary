package com.tosta.ukrainiandictionary.model.entity

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "entries")
data class Entry(
    @Id var slug: String? = null,
    @Column(name = "ukrainian") var ukrainian: String? = null,
    @Column(name = "russian") var russian: String? = null,
    @Column(name = "english") var english: String? = null,
) {

    fun toDto(): EntryDto {
        return EntryDto(
            slug = slug!!,
            ukrainian = ukrainian!!,
            russian = russian!!,
            english = english!!,
        )
    }
}
