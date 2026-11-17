package com.example.moviebox.feature.home.presentation.components

import android.R.attr.radius
import android.R.attr.scaleX
import android.R.attr.scaleY
import android.R.attr.startY
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow


private val samplePosters = listOf(
    "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
    "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
    "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
    "https://image.tmdb.org/t/p/w500/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
    "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg"
)

@Composable
fun MovieCarousel(
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val gradientHeight = size.height * 0.4f
                val top = (size.height - gradientHeight) / 2f
                val center = Offset(
                    x = size.width / 2f,
                    y = size.height / 2f
                )
                //عرض بیضی
                val radius = size.width * 1f
                //ارتفاع بیضی
                val verticalRadius = size.height * 0.28f
                scale(
                    scaleX = 1f,
                    scaleY = verticalRadius / radius,
                    pivot = center
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x80FF1937),
                                Color(0x33FF1937),
                                Color.Transparent
                            )
                            ,
                            center = center, radius = radius
                        ),
                        radius = radius, center = center
                    )
                }
            },
        contentAlignment = Alignment.Center

    ) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .height(340.dp),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(samplePosters) { index, posterUrl ->
                val layoutInfo = listState.layoutInfo

                val itemInfo = layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == index }

                val progress = if (itemInfo != null) {
                    val viewportCenter =
                        (layoutInfo.viewportStartOffset +
                                layoutInfo.viewportEndOffset) / 2f

                    val itemCenter =
                        itemInfo.offset + itemInfo.size / 2f

                    val distance =
                        abs(viewportCenter - itemCenter)

                    val maxDistance = layoutInfo.viewportSize.width / 2f

                    (distance / maxDistance.coerceAtLeast(1f)).coerceIn(0f, 1f)
                } else {
                    1f
                }
                val easedProgress = progress.pow(1.9f)
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(340.dp)
                        .matchParentSize(),
                    contentAlignment = Alignment.Center

                ) {
                    MovieCard(
                        posterUrl = posterUrl,
                        brightness = 1f-(easedProgress * 0.18f),
                        modifier = Modifier
                            .width(160.dp)
                            .height(300.dp)
                            .graphicsLayer {

                                val centerScale = 1f - (easedProgress * 0.25f)

                                scaleX = centerScale + 0.25f
                                scaleY=centerScale + 0.25f
                                translationY=-(1f-easedProgress)*10.dp.toPx()
                                alpha=1f-(easedProgress*0.4f)
                            }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun movieCarouselPreview() {
    MovieCarousel()

}