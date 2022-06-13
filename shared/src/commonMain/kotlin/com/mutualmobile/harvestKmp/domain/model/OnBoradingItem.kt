package com.mutualmobile.harvestKmp.domain.model

import com.mutualmobile.harvestKmp.MR
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource


data class OnBoardingItem(
    val image: ImageResource,
    val color: Long,
    val title: StringResource,
    val colorBottom: Long
)

val onBoardingItems = listOf(
    OnBoardingItem(
        image = MR.images.onboarding_screen_1,
        color = 0xFFF06808,
        title = MR.strings.onboarding_text_1,
        colorBottom = 0xFF95d00
    ),
    OnBoardingItem(
        image = MR.images.onboarding_screen_2,
        color = 0xFF34aca8,
        title = MR.strings.onboarding_text_2,
        colorBottom = 0xFF21a5a0
    ),
    OnBoardingItem(
        image = MR.images.onboarding_screen_3,
        color = 0xFFce6665,
        title = MR.strings.onboarding_text_3,
        colorBottom = 0xFFcb5c5b
    ),
    OnBoardingItem(
        image = MR.images.onboarding_screen_4,
        color = 0xFF4ca5c5,
        title = MR.strings.onboarding_text_4,
        colorBottom = 0xFF3589a6
    )
)