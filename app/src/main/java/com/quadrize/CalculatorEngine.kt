package com.quadrize

class CalculatorEngine {

    fun calculate(first: Double, second: Double, operator: String): Double {
        return when (operator) {
            "+" -> first + second
            "-" -> first - second
            "*", "×" -> first * second
            "/", "÷" -> if (second != 0.0) first / second else Double.NaN
            else -> 0.0
        }
    }
}