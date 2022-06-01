package com.mutualmobile.harvestKmp.domain.model


data class OnBoardingItem(
    val image: Int,
    val color: Long,
    val title: String,
    val colorBottom: Long
)

val onBoardingItem = listOf(
    OnBoardingItem(
        image = 0,
        color = 0xFFF06808,
        title = "Simple, powerful time tracking, reporting, and invoicing",
        colorBottom = 0xFF95d00
    ),
    OnBoardingItem(
        image = 0,
        color = 0xFF34aca8,
        title = "Track time easily, wherever you are",
        colorBottom = 0xFF21a5a0
    ),
    OnBoardingItem(
        image = 0,
        color = 0xFFce6665,
        title = "Enter expense on the go",
        colorBottom = 0xFFcb5c5b
    ),
    OnBoardingItem(
        image = 0,
        color = 0xFF4ca5c5,
        title = "Report on time for powerful insights",
        colorBottom = 0xFF3589a6
    )
)