package com.tosta.ukrainiandictionary.controller

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import org.springframework.http.ResponseEntity

interface EntryController {

    fun add(dto: EntryDto): ResponseEntity<Unit>

    fun remove(id: Long): ResponseEntity<Unit>

    fun fetch(id: Long): ResponseEntity<EntryDto>

    fun fetchAll(): ResponseEntity<List<EntryDto>>

    fun update(id: Long, dto: EntryDto): ResponseEntity<Unit>
}