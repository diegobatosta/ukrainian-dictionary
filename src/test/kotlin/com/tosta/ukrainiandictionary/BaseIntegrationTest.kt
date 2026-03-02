package com.tosta.ukrainiandictionary

import com.tosta.ukrainiandictionary.repository.EntryRepository
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.ExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
abstract class BaseIntegrationTest {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    protected lateinit var webClient: WebTestClient

    @Autowired
    protected lateinit var entryRepository: EntryRepository

    @AfterEach
    protected fun clearDatabase() = entryRepository.deleteAll()

    protected fun getHost(): String = "http://localhost:$port"

    protected fun getEntryId(result: ExchangeResult): Long =
        result.responseHeaders.location!!.path.substringAfterLast("/").toLong()
}
