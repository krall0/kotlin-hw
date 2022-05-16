package ru.tfs

class Translator {

    private val ruEnDictionary = mapOf(
        "Тойота" to "Toyota",
        "Хонда" to "Honda",
        "Субару" to "Subaru",
        "Ауди" to "Audi",
        "Форд" to "Ford",
        "Камри" to "Camri",
        "Фит" to "Fit",
        "Аутбэк" to "Outback",
        "А5" to "A5",
        "Фокус" to "Focus",
        "Седан" to "Sedan",
        "Хетчбэк" to "Hatchback",
        "Универсал" to "Wagon",
        "Купе" to "Coupe"
    )

    fun translate(word: String): String {
        return ruEnDictionary.getOrDefault(word, "")
    }
}
