package com.tosta.ukrainiandictionary.service

import com.tosta.ukrainiandictionary.model.entity.Entry
import com.tosta.ukrainiandictionary.repository.EntryRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DefaultEntryService(private val repository: EntryRepository) : EntryService {

    override fun findBySlug(slug: String): Entry? = repository.findBySlug(slug)

    override fun findByQuery(query: String): List<Entry> = repository.findByQuery(query)

    @Transactional
    override fun add(entry: Entry): Entry = repository.save(entry)

    @Transactional
    override fun update(slug: String, entry: Entry) {
        repository.deleteBySlug(slug)
        repository.save(entry)
    }

    @Transactional
    override fun delete(slug: String) = repository.deleteBySlug(slug)
}
