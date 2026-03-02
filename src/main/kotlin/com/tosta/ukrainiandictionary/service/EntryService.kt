package com.tosta.ukrainiandictionary.service

import com.tosta.ukrainiandictionary.model.entity.Entry

interface EntryService {

    fun save(entry: Entry): Entry

    fun delete(id: Long)

    fun find(id: Long): Entry?

    fun findAll(): List<Entry>
}