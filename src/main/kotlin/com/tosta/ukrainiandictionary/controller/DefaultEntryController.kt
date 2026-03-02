package com.tosta.ukrainiandictionary.controller

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import com.tosta.ukrainiandictionary.service.EntryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class DefaultEntryController(private val service: EntryService) : EntryController {

    @PostMapping("/entries")
    override fun add(@RequestBody dto: EntryDto): ResponseEntity<Unit> {
        val entry = service.save(dto.toEntity())
        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entry.id).toUri()

        return ResponseEntity.created(location).build()
    }

    @DeleteMapping("/entries/{id}")
    override fun remove(@PathVariable id: Long): ResponseEntity<Unit> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/entries/{id}")
    override fun fetch(@PathVariable id: Long): ResponseEntity<EntryDto> {
        return service.find(id)?.let { ResponseEntity.ok(it.toDto()) } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/entries")
    override fun fetchAll(): ResponseEntity<List<EntryDto>> {
        val entries = service.findAll()
        return ResponseEntity.ok(entries.map { it.toDto() })
    }

    @PutMapping("/entries/{id}")
    override fun update(@PathVariable id: Long, @RequestBody dto: EntryDto): ResponseEntity<Unit> {
        if (service.find(id) == null) return ResponseEntity.notFound().build()

        service.save(dto.toEntity(id))
        return ResponseEntity.noContent().build()
    }
}
