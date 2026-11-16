package com.quadrize

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

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
                        _resultText.value = "Error"
                    }
                }
                return
            }

            if (isCalculated) {
                val currentResult = _resultText.value ?: ""
                isCalculated = false
                _resultText.value = ""

                if (isOperator(btn)) {
                    if (currentResult != "Error") {
                        _equatiionText.value = currentResult + btn.symbol
                    } else {
                        _equatiionText.value = btn.symbol
                    }
                } else {
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
        return try {
            val rhino = Context.enter()
            rhino.optimizationLevel = -1
            val scriptable: Scriptable = rhino.initStandardObjects()
            var finalResult = rhino.evaluateString(scriptable, equation, "Javascript", 1, null).toString()

            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "")
            }
            Context.exit()
            finalResult
        } catch (e: Exception) {
            "Error"
        }
    }
}