package ru.tfs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SalesManagerTest {

    private val salesAmount = 150.toBigDecimal()
    private val pricePerSale = 10.toBigDecimal()
    private val bonus = 20.toBigDecimal()

    @Test
    fun `when salesPerMonthAmount greater than limit then monthly bonus must be paid`() {
        val salesManager = SalesManager(salesAmount, pricePerSale)
        val limit = 100.toBigDecimal()

        salesManager.calculateMonthlyBonus(limit, bonus)

        assertEquals(bonus, salesManager.getBankAccount())
    }

    @Test
    fun `when salesPerMonthAmount equals limit then monthly bonus must be paid`() {
        val salesManager = SalesManager(salesAmount, pricePerSale)
        val limit = 150.toBigDecimal()

        salesManager.calculateMonthlyBonus(limit, bonus)

        assertEquals(bonus, salesManager.getBankAccount())
    }

    @Test
    fun `when salesPerMonthAmount lower than limit then monthly bonus must not be paid`() {
        val salesManager = SalesManager(salesAmount, pricePerSale)
        val limit = 200.toBigDecimal()

        salesManager.calculateMonthlyBonus(limit, bonus)

        assertEquals(BigDecimal.ZERO, salesManager.getBankAccount())
    }
}