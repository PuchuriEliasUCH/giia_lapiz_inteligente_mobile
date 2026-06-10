package com.giia.lapiz_inteligente.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

const val ANIM_DURATION_MS = 0

val noAnimTween = tween<Float>(durationMillis = ANIM_DURATION_MS)
val noAnimFadeIn = fadeIn(animationSpec = noAnimTween)
val noAnimFadeOut = fadeOut(animationSpec = noAnimTween)
