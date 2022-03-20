package ru.tfs

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ContractorTest {

    private val monthCount = 3

    @MockK(relaxUnitFun = true)
    lateinit var salesManager: SalesManager

    @SpyK
    var worker = WarehouseWorker(50.toBigDecimal())

    @Test
    fun `test contractor pay to salesManager`() {
        val contractor = Contractor(salesManager)
        every { salesManager.getContractSum(monthCount) } returns 100.toBigDecimal()

        contractor.pay(monthCount)

        assertAll(
            { verify(exactly = 1) { salesManager.getContractSum(monthCount) } },
            { verify(exactly = 1) { salesManager.payWages(100.toBigDecimal()) } }
        )
    }

    @Test
    fun `test contractor pay to warehouseWorker`() {
        val contractor = Contractor(worker)

        contractor.pay(monthCount)

        assertAll(
            { verify(exactly = 1) { worker.getContractSum(monthCount) } },
            { verify(exactly = 1) { worker.payWages(150.toBigDecimal()) } },
            { assertEquals(150.toBigDecimal(), worker.getSalaryAccount()) }
        )
    }
}
