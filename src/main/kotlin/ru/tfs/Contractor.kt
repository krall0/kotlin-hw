package ru.tfs

import java.math.BigDecimal

class Contractor(private val employee: Employee) {

    fun pay(monthCount: Int) {
        val contractSum: BigDecimal = employee.getContractSum(monthCount)
        employee.payWages(contractSum)
    }
}
