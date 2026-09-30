package com.quadrize

class CalculatorEngine {

    fun calculate(first: Double, second: Double, operator: String): Double {
        return when (operator) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> first / second
            else -> 0.0
        }
    }
}