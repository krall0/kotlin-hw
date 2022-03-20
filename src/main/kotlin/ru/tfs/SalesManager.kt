package ru.tfs

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class SalesManager(private val salesPerMonthAmount: BigDecimal, private val pricePerSale: BigDecimal) : Employee {

    private var bankAccount: BigDecimal = ZERO

    override fun payWages(sum: BigDecimal) {
        bankAccount += sum
        println("SalesManager's salary paid: $sum, total amount: $bankAccount")
    }

    override fun getContractSum(monthCount: Int): BigDecimal {
        return salesPerMonthAmount * pricePerSale * monthCount.toBigDecimal()
    }

    fun calculateMonthlyBonus(salesAmountLimit: BigDecimal, bonusAmount: BigDecimal) {
        if (salesPerMonthAmount >= salesAmountLimit) {
            bankAccount += bonusAmount
            println("SalesManager's bonus paid: $bonusAmount, total amount: $bankAccount")
        }
    }

    fun getBankAccount() : BigDecimal = bankAccount
}
