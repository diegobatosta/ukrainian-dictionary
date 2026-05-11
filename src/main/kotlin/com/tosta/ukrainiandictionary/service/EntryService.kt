package com.tosta.ukrainiandictionary.service

import com.tosta.ukrainiandictionary.model.entity.Entry

interface EntryService {

    fun findBySlug(slug: String): Entry?

    fun findByQuery(query: String): List<Entry>

    fun add(entry: Entry): Entry

    fun update(slug: String, entry: Entry)

    fun delete(slug: String)
}
