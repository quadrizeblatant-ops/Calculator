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
    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equatiionText.value?.let {
            if (btn == "AC") {
                _equatiionText.value = ""
                _resultText.value = "0"
                return
            }
            if (btn == "C") {
                if (it.isNotEmpty()) {
                    _equatiionText.value = it.substring(0, it.length - 1)
                    return
                }
            }
            if (btn == "=") {
                try {
                    _resultText.value = calculateResult(it)
                } catch (_: Exception) {
                }

                return
            }
            _equatiionText.value = it + btn

            try {
                _resultText.value = calculateResult(_equatiionText.value.toString())
            } catch (_: Exception) {
            }
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