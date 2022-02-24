package ru.tfs

fun main() {
    val worker = WarehouseWorker(100.00.toBigDecimal())
    worker.payWages(10000)
    worker.takeDayOff()

    val manager = SalesManager(
        salesPerMonthAmount = 15.00.toBigDecimal(),
        pricePerSale = 100.00.toBigDecimal()
    )
    manager.payWages(1000.00.toBigDecimal())
    manager.calculateMonthlyBonus(10.00.toBigDecimal(), 20.00.toBigDecimal())

    listOf(manager, worker).forEach {
        it.payWages(10000)
    }

    val contractor = Contractor(manager)
    contractor.pay(monthCount = 3)
}
