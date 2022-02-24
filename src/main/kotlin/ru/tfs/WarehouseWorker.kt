package ru.tfs

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf

class WarehouseWorker(private val monthlyRate: BigDecimal) : Employee {

    private var extraDaysOff: Int = 5

    private var salaryAccount: BigDecimal = ZERO

    override fun payWages(sum: BigDecimal) {
        salaryAccount += sum
        println("WarehouseWorker's salary paid: $sum, total amount: $salaryAccount")
    }

    override fun getContractSum(monthCount: Int): BigDecimal {
        return monthlyRate * valueOf(monthCount.toLong())
    }

    fun takeDayOff() {
        extraDaysOff--
        println("WarehouseWorker taked extra day off, remained: $extraDaysOff")
    }
}
