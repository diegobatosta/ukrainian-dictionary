package com.tosta.ukrainiandictionary.model.entity

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import jakarta.persistence.*

@Entity
@Table(name = "entries")
data class Entry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @Column(name = "in_ukrainian") var inUkrainian: String? = null,

    @Column(name = "in_russian") var inRussian: String? = null,

    @Column(name = "in_english") var inEnglish: String? = null,
) {
    fun toDto(): EntryDto {
        return EntryDto(
            inUkrainian = inUkrainian,
            inRussian = inRussian,
            inEnglish = inEnglish,
        )
    }
}