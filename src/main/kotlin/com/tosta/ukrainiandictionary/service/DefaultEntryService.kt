package com.tosta.ukrainiandictionary.service

import com.tosta.ukrainiandictionary.model.entity.Entry
import com.tosta.ukrainiandictionary.repository.EntryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DefaultEntryService(private val repository: EntryRepository) : EntryService {

    override fun save(entry: Entry): Entry {
        return repository.save(entry)
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    override fun find(id: Long): Entry? {
        return repository.findByIdOrNull(id)
    }

    override fun findAll(): List<Entry> {
        return repository.findAll()
    }
}