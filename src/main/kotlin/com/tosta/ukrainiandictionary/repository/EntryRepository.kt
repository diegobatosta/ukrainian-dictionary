package com.tosta.ukrainiandictionary.repository

import com.tosta.ukrainiandictionary.model.entity.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : JpaRepository<Entry, Long>