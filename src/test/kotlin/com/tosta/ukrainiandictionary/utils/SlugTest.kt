package com.tosta.ukrainiandictionary.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SlugTest {

    @Test
    fun `GIVEN an expression with multiple spaces, WHEN it is slugified, THEN they map to a single dash`() {
        // Act & Assert
        assertThat("тому   що".slugify()).isEqualTo("tomu-shcho")
    }

    @Test
    fun `GIVEN an expression with non-letter, non-hyphen, and non-space characters, WHEN it is slugified, THEN they are ignored`() {
        // Act & Assert
        assertThat("привіт, ромашки!".slugify()).isEqualTo("pryvit-romashky")
    }

    @Test
    fun `GIVEN an expression with leading and trailing spaces, WHEN it is slugified, THEN they are ignored`() {
        // Act & Assert
        assertThat("  все одно  ".slugify()).isEqualTo("vse-odno")
    }

    @Test
    fun `GIVEN an expression with numerals, WHEN it is slugified, THEN they are included`() {
        // Act & Assert
        assertThat("у мене 2 черепахи".slugify()).isEqualTo("u-mene-2-cherepakhy")
    }

    @Test
    fun `GIVEN an expression with a hyphen, WHEN it is slugified, THEN the hyphen is included`() {
        // Act & Assert
        assertThat("подруга-фармацевт".slugify()).isEqualTo("podruha-farmatsevt")
    }
}
