package com.tosta.ukrainiandictionary.utils

private val transliteration = mapOf(
    'а' to "a",
    'б' to "b",
    'в' to "v",
    'г' to "h",
    'ґ' to "g",
    'д' to "d",
    'е' to "e",
    'є' to "ye",
    'ж' to "zh",
    'з' to "z",
    'и' to "y",
    'і' to "i",
    'ї' to "yi",
    'й' to "y",
    'к' to "k",
    'л' to "l",
    'м' to "m",
    'н' to "n",
    'о' to "o",
    'п' to "p",
    'р' to "r",
    'с' to "s",
    'т' to "t",
    'у' to "u",
    'ф' to "f",
    'х' to "kh",
    'ц' to "ts",
    'ч' to "ch",
    'ш' to "sh",
    'щ' to "shch",
    'ь' to "",
    'ю' to "yu",
    'я' to "ya",
    'ʼ' to "",
    '\'' to "",
)

fun String.slugify(): String {
    return this
        .lowercase()
        .map { transliteration[it] ?: it.toString() }
        .joinToString("")
        .replace(Regex("[^\\w\\s-]+"), "")
        .replace(Regex("[\\s_]+"), "-")
        .trim('-')
}
