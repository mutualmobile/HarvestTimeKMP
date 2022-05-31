package com.mutualmobile.harvestKmp.domain.model


data class OnBoardingItem(
    val image: Int,
    val color: Long,
    val title: String,
    val colorBottom: Long
)

val onBoardingItem = listOf(
    OnBoardingItem(
        0,
        0xFFF06808,
        "Simple,powerful time tracking, reporting, and invoicing",
        0xFF95d00
    ),
    OnBoardingItem(
        0,
        0xFF34aca8,
        "Track time easily, wherever you are",
        0xFF21a5a0
    ),
    OnBoardingItem(
        0,
        0xFFce6665,
        "Enter expense on the go",
        0xFFcb5c5b
    ),
    OnBoardingItem(
        0,
        0xFF4ca5c5,
        "Report on time for powerful insights",
        0xFF3589a6
    )
)