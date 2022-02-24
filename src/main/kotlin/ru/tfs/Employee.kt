package ru.tfs

import java.math.BigDecimal

interface Employee {

    fun payWages(sum: BigDecimal)

    fun payWages(sum: Long) = payWages(BigDecimal.valueOf(sum, 2))

    fun getContractSum(monthCount: Int): BigDecimal
}
