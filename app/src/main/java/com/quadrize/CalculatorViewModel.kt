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
    fun onButtonClick(btn: ButtonList) {
        Log.i("Clicked Button", btn.symbol)

        _equatiionText.value?.let { equation ->

            if (btn == ButtonList.AC) {
                _equatiionText.value = ""
                _resultText.value = ""
                return
            }

            if (btn == ButtonList.C) {
                if (equation.isNotEmpty()) {
                    _equatiionText.value = equation.dropLast(1)
                    return
                }
            }

            if (btn == ButtonList.EQUALS) {
                try {
                    _resultText.value = calculateResult(equation)
                } catch (_: Exception) {
                }
                return
            }

            _equatiionText.value = equation + btn.symbol
        }
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

        val firstNumber = numbers[0].toDouble()
        val secondNumber = numbers[1].toDouble()

        return calculatorEngine
            .calculate(firstNumber, secondNumber, operator)
            .toString()
    }
}