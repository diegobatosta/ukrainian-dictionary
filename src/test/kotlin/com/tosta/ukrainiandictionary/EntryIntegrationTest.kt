package com.tosta.ukrainiandictionary

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import com.tosta.ukrainiandictionary.model.entity.Entry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.reactive.server.expectBodyList

class EntryIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `GIVEN a valid entry, WHEN it is added, THEN the entry is saved and returned`() {
        // Arrange
        val entryDto = EntryDto(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )

        // Act & Assert
        val result =
            webClient.post().uri("/entries").bodyValue(entryDto).exchange().expectStatus().isCreated.expectHeader()
                .valueMatches("Location", "${getHost()}/entries/\\d+").returnResult()
        assertThat(
            entryRepository.findByIdOrNull(getEntryId(result))
        ).isNotNull.extracting("inUkrainian", "inRussian", "inEnglish").containsExactly("привіт", "привет", "hello")
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is removed, THEN it is successfully deleted from the database`() {
        // Arrange
        var entry = Entry(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )
        entry = entryRepository.save(entry)

        // Act & Assert
        webClient.delete().uri("/entries/${entry.id}").exchange().expectStatus().isNoContent
        assertThat(entryRepository.existsById(entry.id!!)).isFalse
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is fetched, THEN it is returned`() {
        // Arrange
        var entry = Entry(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )
        entry = entryRepository.save(entry)

        // Act & Assert
        webClient.get().uri("/entries/${entry.id}").exchange().expectStatus().isOk.expectBody(EntryDto::class.java)
            .consumeWith { response ->
                val body = response.responseBody!!
                assertThat(body).extracting("inUkrainian", "inRussian", "inEnglish")
                    .containsExactly("привіт", "привет", "hello")
            }
    }

    @Test
    fun `GIVEN a nonexistent entry in the database, WHEN it is fetched, THEN an error is returned`() {
        // Act & Assert
        webClient.get().uri("/entries/1").exchange().expectStatus().isNotFound
    }

    @Test
    fun `GIVEN two entries in the database, WHEN they are fetched, THEN they are both returned`() {
        // Arrange
        val entryA = Entry(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )
        val entryB = Entry(
            inUkrainian = "бувай",
            inRussian = "пока",
            inEnglish = "bye",
        )
        entryRepository.save(entryA)
        entryRepository.save(entryB)

        // Act & Assert
        val result = webClient.get().uri("/entries").exchange().expectStatus().isOk.expectBodyList<EntryDto>()
            .returnResult().responseBody
        assertThat(result).hasSize(2).extracting("inUkrainian", "inRussian", "inEnglish")
            .containsExactly(Tuple("привіт", "привет", "hello"), Tuple("бувай", "пока", "bye"))
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is updated, THEN the changes are saved`() {
        // Arrange
        var entry = Entry(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )
        entry = entryRepository.save(entry)

        val entryDto = EntryDto(
            inUkrainian = "бувай",
            inRussian = "пока",
            inEnglish = "bye",
        )

        // Act & Assert
        webClient.put().uri("/entries/${entry.id}").bodyValue(entryDto).exchange().expectStatus().isNoContent
        assertThat(entryRepository.findByIdOrNull(entry.id!!)).isNotNull.extracting(
            "inUkrainian", "inRussian", "inEnglish"
        ).containsExactly("бувай", "пока", "bye")
    }

    @Test
    fun `GIVEN a nonexistent entry in the database, WHEN it is updated, THEN an error is returned`() {
        // Arrange
        val entryDto = EntryDto(
            inUkrainian = "привіт",
            inRussian = "привет",
            inEnglish = "hello",
        )

        // Act & Assert
        webClient.put().uri("/entries/1").bodyValue(entryDto).exchange().expectStatus().isNotFound
    }
}
