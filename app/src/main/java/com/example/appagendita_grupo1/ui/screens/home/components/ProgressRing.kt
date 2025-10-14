package com.example.appagendita_grupo1.ui.screens.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.appagendita_grupo1.ui.theme.CardStroke
import com.example.appagendita_grupo1.ui.theme.NavyText
import com.example.appagendita_grupo1.ui.theme.BlueAccent
@Composable
fun ProgressRing(percentage: Int, size: Dp = 48.dp, stroke: Dp = 6.dp) {
  Box(Modifier.size(size), contentAlignment = Alignment.Center) {
    Canvas(Modifier.fillMaxSize()) {
      val sweep = 360f * (percentage / 100f)
      val strokeW = stroke.toPx()
      drawArc(
        color = CardStroke.copy(alpha = .4f),
        startAngle = -90f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(width = strokeW, cap = StrokeCap.Round),
        size = this.size
      )
      drawArc(
        color = BlueAccent,
        startAngle = -90f,
        sweepAngle = sweep,
        useCenter = false,
        style = Stroke(width = strokeW, cap = StrokeCap.Round),
        size = this.size
      )
    }
    Text("${percentage}%", style = MaterialTheme.typography.labelLarge, color = NavyText)
  }
}
