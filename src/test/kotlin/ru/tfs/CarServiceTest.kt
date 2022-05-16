package ru.tfs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CarServiceTest {

    private val translator = Translator()
    private val carService = CarService(translator)

    private val cars = listOf(
        Car("Тойота", "Камри", "Седан", 100_000, 8.0),
        Car("Хонда", "Фит", "Хетчбэк", 30_000, 6.5),
        Car("Субару", "Аутбэк", "Универсал", 200_000, 8.5),
        Car("Ауди", "А5", "Купе", 300_000, 10.0),
        Car("Форд", "Фокус", "Седан", 50_000, 7.5)
    )

    @Test
    fun `test for translating and converting price method`() {

        val expected = listOf(
            Car("Honda", "Fit", "Hatchback", 300_000, 6.5),
            Car("Ford", "Focus", "Sedan", 500_000, 7.5),
            Car("Toyota", "Camri", "Sedan", 1_000_000, 8.0),
            Car("Subaru", "Outback", "Wagon", 2_000_000, 8.5),
            Car("Audi", "A5", "Coupe", 3_000_000, 10.0)
        )

        val actual = carService.translateAndConvertPrice(cars)

        assertEquals(actual, expected)
    }

    @Test
    fun `test for grouping by body type`() {

        val expected = mapOf(
            "Седан" to listOf(
                Car("Тойота", "Камри", "Седан", 100_000, 8.0),
                Car("Форд", "Фокус", "Седан", 50_000, 7.5)
            ),
            "Хетчбэк" to listOf(
                Car("Хонда", "Фит", "Хетчбэк", 30_000, 6.5)
            ),
            "Универсал" to listOf(
                Car("Субару", "Аутбэк", "Универсал", 200_000, 8.5)
            ),
            "Купе" to listOf(
                Car("Ауди", "А5", "Купе", 300_000, 10.0)
            )
        )

        val actual = carService.groupByBodyType(cars)

        assertEquals(actual, expected)
    }

    @Test
    fun `test for first 3 cars with price lower than 250_000`() {

        val expected = listOf(
            "Тойота Камри",
            "Хонда Фит",
            "Субару Аутбэк"
        )

        val actual = carService.filterByPriceLower(cars, 250_000)

        assertEquals(actual, expected)
    }
}
