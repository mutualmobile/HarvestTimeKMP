package com.mutualmobile.harvestKmp.android.ui.screens.onboradingScreen.components


data class OnBoardingItem(
   val image: Int,
   val color: Long,
   val title: String,
   val desc: String
)

val onBoardingItem = listOf(
   OnBoardingItem(
      0,
       0xFFFF0000,
      "Simple,powerful time tracking, reporting, and invoicing",
      "Lorem Ipsum is simply dummy text of the printing and typesetting Industry."
   ),
   OnBoardingItem(
     0,
       0xFF0000FF,
      "Track time easily, wherever you are",
      "Lorem Ipsum is simply dummy text of the printing and typesetting Industry."
   ),
   OnBoardingItem(
     0,
       0xFFFFFF00,
      "Enter expense on the go",
      "Lorem Ipsum is simply dummy text of the printing and typesetting Industry."
   ),
    OnBoardingItem(
     0,
       0xFFADFF00,
      "Report on time for powerful insights",
      "Lorem Ipsum is simply dummy text of the printing and typesetting Industry."
   )
)