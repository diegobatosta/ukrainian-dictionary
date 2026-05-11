package com.tosta.ukrainiandictionary

import com.tosta.ukrainiandictionary.model.dto.EntryDto
import com.tosta.ukrainiandictionary.model.entity.Entry
import com.tosta.ukrainiandictionary.utils.slugify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.expectBodyList

class EntryIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `GIVEN a valid entry, WHEN it is added, THEN the entry is saved and returned`() {
        // Arrange
        val entryDto = EntryDto(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )

        // Act & Assert
        val result =
            webClient.post().uri("/entries").bodyValue(entryDto).exchange().expectStatus().isCreated
                .expectHeader()
                .valueMatches("Location", "${getHost()}/entries/\\w+")
                .returnResult()
        assertThat(entryRepository.findBySlug(getSlug(result))).isNotNull
            .extracting("ukrainian", "russian", "english")
            .containsExactly("привіт", "привет", "hello")
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is removed, THEN it is successfully deleted from the database`() {
        // Arrange
        var entry = Entry(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )
        entry = entryRepository.save(entry)

        // Act & Assert
        webClient.delete().uri("/entries/${entry.slug}").exchange().expectStatus().isNoContent
        assertThat(entryRepository.existsBySlug(entry.slug!!)).isFalse
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is fetched, THEN it is returned`() {
        // Arrange
        var entry = Entry(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )
        entry = entryRepository.save(entry)

        // Act & Assert
        webClient.get().uri("/entries/${entry.slug}").exchange().expectStatus().isOk
            .expectBody(EntryDto::class.java)
            .consumeWith { response ->
                val body = response.responseBody!!
                assertThat(body)
                    .extracting("ukrainian", "russian", "english")
                    .containsExactly("привіт", "привет", "hello")
            }
    }

    @Test
    fun `GIVEN a nonexistent entry in the database, WHEN it is fetched, THEN an error is returned`() {
        // Act & Assert
        webClient.get().uri("/entries/pryvit").exchange().expectStatus().isNotFound
    }

    @Test
    fun `GIVEN entries in the database, WHEN fetching entries without a query, THEN an error is returned`() {
        // Arrange
        val entryA = Entry(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )
        val entryB = Entry(
            slug = "бувай".slugify(),
            ukrainian = "бувай",
            russian = "пока",
            english = "bye",
        )
        entryRepository.saveAll(listOf(entryA, entryB))

        // Act & Assert
        webClient.get().uri("/entries").exchange().expectStatus().isBadRequest
    }

    @Test
    fun `GIVEN similar entries in the database, WHEN a related query is made, THEN they are returned successfully`() {
        // Arrange
        val entryA = Entry(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )
        val entryB = Entry(
            slug = "поки".slugify(),
            ukrainian = "поки",
            russian = "пока",
            english = "while",
        )
        val entryC = Entry(
            slug = "поки що".slugify(),
            ukrainian = "поки що",
            russian = "пока что",
            english = "so far",
        )
        entryRepository.saveAll(listOf(entryA, entryB, entryC))

        // Act
        val result = webClient.get().uri { uriBuilder ->
            uriBuilder.path("/entries").queryParam("query", "поки").build()
        }.exchange().expectStatus().isOk.expectBodyList<EntryDto>().returnResult().responseBody

        // Assert
        assertThat(result)
            .hasSize(2)
            .extracting("ukrainian", "russian", "english")
            .containsExactly(Tuple("поки", "пока", "while"), Tuple("поки що", "пока что", "so far"))
    }

    @Test
    fun `GIVEN an entry in the database, WHEN it is updated, THEN the changes are saved`() {
        // Arrange
        var entry = Entry(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )
        entry = entryRepository.save(entry)

        val entryDto = EntryDto(
            slug = "бувай".slugify(),
            ukrainian = "бувай",
            russian = "пока",
            english = "bye",
        )

        // Act & Assert
        webClient
            .put()
            .uri("/entries/${entry.slug}")
            .bodyValue(entryDto)
            .exchange()
            .expectStatus().isNoContent
        assertThat(entryRepository.findBySlug("buvay")).isNotNull
            .extracting("ukrainian", "russian", "english")
            .containsExactly("бувай", "пока", "bye")
    }

    @Test
    fun `GIVEN a nonexistent entry in the database, WHEN it is updated, THEN an error is returned`() {
        // Arrange
        val entryDto = EntryDto(
            slug = "привіт".slugify(),
            ukrainian = "привіт",
            russian = "привет",
            english = "hello",
        )

        // Act & Assert
        webClient
            .put()
            .uri("/entries/pryvit")
            .bodyValue(entryDto)
            .exchange()
            .expectStatus().isNotFound
    }
}
