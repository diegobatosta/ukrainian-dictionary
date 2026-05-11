package com.tosta.ukrainiandictionary.repository

import com.tosta.ukrainiandictionary.model.entity.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : JpaRepository<Entry, String> {

    fun findBySlug(slug: String): Entry?

    @Query(
        value = """
        SELECT *
        FROM entries
        WHERE ukrainian ILIKE %:query% OR russian ILIKE %:query% OR english ILIKE %:query%
        """,
        nativeQuery = true,
    )
    fun findByQuery(@Param("query") query: String): List<Entry>

    fun deleteBySlug(slug: String)

    fun existsBySlug(slug: String): Boolean
}
