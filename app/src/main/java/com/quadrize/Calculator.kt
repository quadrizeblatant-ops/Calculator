package com.quadrize

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

val buttonList = listOf(
    ButtonList.C,
    ButtonList.OPEN_BRACKET,
    ButtonList.CLOSE_BRACKET,
    ButtonList.DIVIDE,

    ButtonList.SEVEN,
    ButtonList.EIGHT,
    ButtonList.NINE,
    ButtonList.MULTIPLY,

    ButtonList.FOUR,
    ButtonList.FIVE,
    ButtonList.SIX,
    ButtonList.PLUS,

    ButtonList.ONE,
    ButtonList.TWO,
    ButtonList.THREE,
    ButtonList.MINUS,

    ButtonList.AC,
    ButtonList.ZERO,
    ButtonList.DOT,
    ButtonList.EQUALS
)

@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {
    val equationText by viewModel.equationText.observeAsState("")
    val resultText by viewModel.resultText.observeAsState("")

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Living animated gradient background
        GlassmorphicBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Glass display card for expression & result
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(Color(0x40FFFFFF), Color(0x10FFFFFF))
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Equation display with smooth animation
                    AnimatedContent(
                        targetState = equationText.ifEmpty { "0" },
                        transitionSpec = {
                            (slideInVertically { height -> height / 2 } + fadeIn()) togetherWith
                                    (slideOutVertically { height -> -height / 2 } + fadeOut())
                        },
                        label = "equationText"
                    ) { text ->
                        Text(
                            text = text,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (equationText.isEmpty()) Color(0x60FFFFFF) else Color(0xB3FFFFFF),
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Result display with spring animation
                    AnimatedContent(
                        targetState = resultText,
                        transitionSpec = {
                            (slideInVertically { height -> height } + fadeIn()) togetherWith
                                    (slideOutVertically { height -> -height } + fadeOut())
                        },
                        label = "resultText"
                    ) { result ->
                        Text(
                            text = result.ifEmpty { " " },
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Glassmorphic Buttons Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(buttonList) { btn ->
                    CalculatorButton(
                        btn = btn,
                        onClick = { viewModel.onButtonClick(btn) }
                    )
                }
            }
        }
    }
}

@Composable
fun GlassmorphicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "bgAnimation")

    val angle1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb1Angle"
    )

    val angle2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb2Angle"
    )

    val rad1 = Math.toRadians(angle1.toDouble())
    val rad2 = Math.toRadians(angle2.toDouble())

    val orb1X = (sin(rad1) * 0.35 + 0.35).toFloat()
    val orb1Y = (cos(rad1) * 0.25 + 0.25).toFloat()

    val orb2X = (cos(rad2) * 0.3 + 0.65).toFloat()
    val orb2Y = (sin(rad2) * 0.35 + 0.7).toFloat()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep rich dark backdrop
        drawRect(color = Color(0xFF0C0A15))

        // Neon Violet glowing orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF7000FF).copy(alpha = 0.5f),
                    Color(0xFF4B0082).copy(alpha = 0.2f),
                    Color.Transparent
                ),
                center = Offset(w * orb1X, h * orb1Y),
                radius = w * 0.9f
            ),
            center = Offset(w * orb1X, h * orb1Y),
            radius = w * 0.9f
        )

        // Neon Cyan glowing orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00F2FE).copy(alpha = 0.38f),
                    Color(0xFF4FACFE).copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = Offset(w * orb2X, h * orb2Y),
                radius = w * 1.0f
            ),
            center = Offset(w * orb2X, h * orb2Y),
            radius = w * 1.0f
        )

        // Center Magenta/Pink glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF007F).copy(alpha = 0.22f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.55f),
                radius = w * 0.75f
            ),
            center = Offset(w * 0.5f, h * 0.55f),
            radius = w * 0.75f
        )
    }
}

@Composable
fun CalculatorButton(btn: ButtonList, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Haptic feedback on touch
    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    // Spring press scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .background(getButtonBackgroundBrush(btn))
            .border(
                width = 1.dp,
                brush = getButtonBorderBrush(btn),
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = btn.symbol,
            fontSize = if (btn.symbol.length > 1) 20.sp else 26.sp,
            fontWeight = if (isOperatorOrEquals(btn)) FontWeight.Bold else FontWeight.SemiBold,
            color = getButtonTextColor(btn)
        )
    }
}

private fun isOperatorOrEquals(btn: ButtonList): Boolean {
    return btn in listOf(
        ButtonList.DIVIDE,
        ButtonList.MULTIPLY,
        ButtonList.PLUS,
        ButtonList.MINUS,
        ButtonList.EQUALS
    )
}

private fun getButtonBackgroundBrush(btn: ButtonList): Brush {
    return when (btn) {
        ButtonList.EQUALS -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF00F2FE),
                Color(0xFF9B51E0)
            )
        )

        ButtonList.PLUS, ButtonList.MINUS, ButtonList.MULTIPLY, ButtonList.DIVIDE -> Brush.verticalGradient(
            colors = listOf(
                Color(0x609B51E0),
                Color(0x357B2CBF)
            )
        )

        ButtonList.AC, ButtonList.C -> Brush.verticalGradient(
            colors = listOf(
                Color(0x40FF5252),
                Color(0x20FF1744)
            )
        )

        ButtonList.OPEN_BRACKET, ButtonList.CLOSE_BRACKET -> Brush.verticalGradient(
            colors = listOf(
                Color(0x30FFFFFF),
                Color(0x12FFFFFF)
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0x22FFFFFF),
                Color(0x0CFFFFFF)
            )
        )
    }
}

private fun getButtonBorderBrush(btn: ButtonList): Brush {
    return when (btn) {
        ButtonList.EQUALS -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xEEFFFFFF),
                Color(0x88FFFFFF)
            )
        )

        ButtonList.PLUS, ButtonList.MINUS, ButtonList.MULTIPLY, ButtonList.DIVIDE -> SolidColor(
            Color(0x66D8B4FE)
        )

        ButtonList.AC, ButtonList.C -> SolidColor(
            Color(0x66FF8A80)
        )

        else -> SolidColor(
            Color(0x25FFFFFF)
        )
    }
}

private fun getButtonTextColor(btn: ButtonList): Color {
    return when (btn) {
        ButtonList.EQUALS -> Color.White
        ButtonList.PLUS, ButtonList.MINUS, ButtonList.MULTIPLY, ButtonList.DIVIDE -> Color(0xFFE0AAFF)
        ButtonList.AC, ButtonList.C -> Color(0xFFFF8A80)
        ButtonList.OPEN_BRACKET, ButtonList.CLOSE_BRACKET -> Color(0xFFD1D1D6)
        else -> Color.White
    }
}

