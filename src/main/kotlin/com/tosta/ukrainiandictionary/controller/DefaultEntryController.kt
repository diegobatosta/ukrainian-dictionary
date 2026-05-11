package com.tosta.ukrainiandictionary.controller

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import com.tosta.ukrainiandictionary.service.EntryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@CrossOrigin(origins = [$$"${cross-origin.urls}"])
@RestController
class DefaultEntryController(private val service: EntryService) : EntryController {

    @PostMapping("/entries")
    override fun add(@RequestBody dto: EntryDto): ResponseEntity<Unit> {
        val entry = service.add(dto.toEntity())
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(entry.slug)
            .toUri()

        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/entries/{slug}")
    override fun delete(@PathVariable slug: String): ResponseEntity<Unit> {
        service.delete(slug)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/entries")
    override fun findByQuery(@RequestParam(required = true) query: String): ResponseEntity<List<EntryDto>> {
        return service.findByQuery(query).let { it -> ResponseEntity.ok(it.map { it.toDto() }) }
    }

    @GetMapping("/entries/{slug}")
    override fun findBySlug(@PathVariable slug: String): ResponseEntity<EntryDto> {
        return service.findBySlug(slug)?.let { ResponseEntity.ok(it.toDto()) } ?: ResponseEntity
            .notFound()
            .build()
    }

    @PutMapping("/entries/{slug}")
    override fun update(
        @PathVariable slug: String,
        @RequestBody dto: EntryDto,
    ): ResponseEntity<Unit> {
        if (service.findBySlug(slug) == null) return ResponseEntity.notFound().build()

        service.update(slug, dto.toEntity())
        return ResponseEntity.noContent().build()
    }
}
