package com.quadrize

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.activity.contextaware.ContextAware
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

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
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()
        val finalResult =
            context.evaluateString(scriptable, equation, "Javascript", 1, null).toString()
        return finalResult
    }
}