package com.tosta.ukrainiandictionary.controller

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import org.springframework.http.ResponseEntity

interface EntryController {

    fun add(dto: EntryDto): ResponseEntity<Unit>

    fun update(slug: String, dto: EntryDto): ResponseEntity<Unit>

    fun delete(slug: String): ResponseEntity<Unit>

    fun findBySlug(slug: String): ResponseEntity<EntryDto>

    fun findByQuery(query: String): ResponseEntity<List<EntryDto>>
}
