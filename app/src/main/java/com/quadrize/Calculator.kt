package com.quadrize

import android.R
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background

val buttonList = listOf(
    "C",
    "(",
    ")",
    "/",
    "7",
    "8",
    "9",
    "*",
    "4",
    "5",
    "6",
    "+",
    "1",
    "2",
    "3",
    "-",
    "AC",
    "0",
    ".",
    "="
)

@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {

    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF151020))
    ) {
        Column(
            modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.End
        ) {
            AnimatedContent(
                targetState = equationText.value ?: "", transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn()) togetherWith (slideOutVertically { height -> -height } + fadeOut())
                }) { text ->
                Text(
                    text = text, fontSize = 30.sp, color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            AnimatedContent(
                targetState = resultText.value ?: ""
            ) { result ->
                Text(
                    text = result, style = TextStyle(
                        fontSize = 60.sp, textAlign = TextAlign.End, color = Color.White
                    ), maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
            ) {
                items(buttonList) {
                    CalculatorButton(btn = it, onClick = {
                        viewModel.onButtonClick(it)
                    })
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(btn: String, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(10.dp)) {
        FloatingActionButton(
            onClick = onClick, modifier = Modifier.size(80.dp), containerColor = getColor(btn)
        ) {
            Text(text = btn, fontSize = 20.sp, color = Color.White)
        }
    }
}

fun getColor(btn: String): Color {
    return if (btn in listOf("C", "(", ")", "/") || btn in listOf("/", "*", "+", "-", "=")) {
        Color(0xFF706F6F)
    } else {
        Color.DarkGray
    }
}

