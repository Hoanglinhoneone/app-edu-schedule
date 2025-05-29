package com.hiendao.eduschedule.ui.effect

// TODO: add animation for action navigate screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

fun slideInFromRight() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(350)
)

fun slideInFromLeft() = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = tween(350)
)

fun slideOutToLeft() = slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = tween(350)
)

fun slideOutToRight() = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(350)
)

fun slideInFromBottom() = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(350)
)

fun slideOutToBottom() = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(350)
)