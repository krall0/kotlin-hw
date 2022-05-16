package ru.tfs

class CarService(private val translator: Translator) {

    private val exchangeRate: Int = 10

    fun translateAndConvertPrice(cars: List<Car>): List<Car> {
        return cars.asSequence()
            .map { it.translateAndConvertPrice() }
            .sortedBy { it.price }
            .toList()
    }

    fun groupByBodyType(cars: List<Car>): Map<String, List<Car>> {
        return cars.groupBy { it.bodyType }
    }

    fun filterByPriceLower(cars: List<Car>, maxPrice: Int): List<String> {
        return cars.asSequence()
            .filter { it.price <= maxPrice }
            .map { "${it.brand} ${it.model}" }
            .take(3)
            .toList()
    }

    private fun Car.translateAndConvertPrice(): Car {
        return copy(
            brand = translator.translate(brand),
            model = translator.translate(model),
            bodyType = translator.translate(bodyType),
            price = price * exchangeRate
        )
    }
}
