package com.quadrize

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private val calculatorEngine = CalculatorEngine()
class CalculatorViewModel : ViewModel() {

    private val _equatiionText = MutableLiveData("")
    val equationText: LiveData<String> = _equatiionText

    private val _resultText = MutableLiveData("")
    val resultText: LiveData<String> = _resultText

    private var isCalculated = false

    fun onButtonClick(btn: ButtonList) {
        Log.i("Clicked Button", btn.symbol)

        _equatiionText.value?.let { equation ->

            if (btn == ButtonList.AC) {
                _equatiionText.value = ""
                _resultText.value = ""
                isCalculated = false
                return
            }

            if (btn == ButtonList.C) {
                if (isCalculated) {
                    _equatiionText.value = ""
                    _resultText.value = ""
                    isCalculated = false
                    return
                }
                if (equation.isNotEmpty()) {
                    _equatiionText.value = equation.dropLast(1)
                    return
                }
            }

            if (btn == ButtonList.EQUALS) {
                if (equation.isNotEmpty()) {
                    try {
                        val res = calculateResult(equation)
                        _resultText.value = res
                        isCalculated = true
                    } catch (_: Exception) {
                    }
                }
                return
            }

            // If user clicked an operator/digit after result was already calculated
            if (isCalculated) {
                val currentResult = _resultText.value ?: ""
                isCalculated = false
                _resultText.value = ""

                if (isOperator(btn)) {
                    // Continue calculating using previous result
                    _equatiionText.value = currentResult + btn.symbol
                } else {
                    // Start brand new equation with pressed key
                    _equatiionText.value = btn.symbol
                }
                return
            }

            _equatiionText.value = equation + btn.symbol
        }
    }

    private fun isOperator(btn: ButtonList): Boolean {
        return btn in listOf(
            ButtonList.PLUS,
            ButtonList.MINUS,
            ButtonList.MULTIPLY,
            ButtonList.DIVIDE
        )
    }

    fun calculateResult(equation: String): String {
        val operator = when {
            equation.contains("+") -> "+"
            equation.contains("-") -> "-"
            equation.contains("*") -> "*"
            equation.contains("/") -> "/"
            else -> return equation
        }

        val numbers = equation.split(operator)

        if (numbers.size < 2 || numbers[1].isEmpty()) return equation

        val firstNumber = numbers[0].toDoubleOrNull() ?: return equation
        val secondNumber = numbers[1].toDoubleOrNull() ?: return equation

        val result = calculatorEngine.calculate(firstNumber, secondNumber, operator)
        return if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }
}