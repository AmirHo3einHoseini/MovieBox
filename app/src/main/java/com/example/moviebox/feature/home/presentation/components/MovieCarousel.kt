package com.example.moviebox.feature.home.presentation.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import java.util.concurrent.CancellationException
import kotlin.math.abs
import kotlin.math.pow


private val samplePosters = listOf(
    "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
    "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
    "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
    "https://image.tmdb.org/t/p/w500/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
    "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
    "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
    "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
    "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
    "https://image.tmdb.org/t/p/w500/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
    "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
    "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
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

    val itemStep = with(LocalDensity.current) {
        192.dp.toPx()
    }

    LaunchedEffect(listState) {
        while (true) {
            while (listState.isScrollInProgress) {
                delay(1000)
            }
            delay(2000)
            if (!listState.isScrollInProgress) {
                try {
                    val consumed = listState.animateScrollBy(
                        value = itemStep,
                        animationSpec = tween(
                            durationMillis = 850,
                            easing = FastOutSlowInEasing
                        )
                    )
                    if (consumed < itemStep * 0.5f) {
                        listState.scrollToItem(0)
                    }
                } catch (e: CancellationException) {
                    currentCoroutineContext().ensureActive()

                }
            }
        }
    }

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
                            ),
                            center = center, radius = radius
                        ),
                        radius = radius, center = center
                    )
                }
            },
        contentAlignment = Alignment.Center

    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val horizontalPadding = ((maxWidth - 180.dp) / 2).coerceAtLeast(0.dp)
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .height(340.dp),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = 24.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(samplePosters) { index, posterUrl ->
                    val easedProgressState = remember(listState, index) {

                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            val itemInfo = layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == index }

                            if (itemInfo != null) {
                                val viewportCenter =
                                    (layoutInfo.viewportStartOffset +
                                            layoutInfo.viewportEndOffset) / 2f

                                val itemCenter = itemInfo.offset + itemInfo.size / 2f
                                val distance = abs(viewportCenter - itemCenter)
                                val maxDistance = layoutInfo.viewportSize.width / 2f

                                val progress = (
                                        distance / maxDistance.coerceAtLeast(1f)
                                        ).coerceIn(0f, 1f)
                                progress.pow(1.9f)

                            } else {
                                1f
                            }
                        }
                    }
                    val easedProgress=easedProgressState.value
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(380.dp)
                            .matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MovieCard(
                            posterUrl = posterUrl,
                            brightness = 1f - (easedProgress * 0.18f),

                            modifier = Modifier
                                .width(160.dp)
                                .height(340.dp)
                                .graphicsLayer {

                                    val centerScale = 1.25f - (easedProgress * 0.35f)
                                    scaleX = centerScale
                                    scaleY = centerScale

                                    translationY = -(1f - easedProgress) * 10.dp.toPx()
                                    alpha = 1f - (easedProgress * 0.4f)
                                }
                        )
                    }
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