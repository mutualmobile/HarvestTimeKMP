package com.mutualmobile.harvestKmp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
  body1 = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
  )
  /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val ReportCardTypography = Typography(
  h1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp
  ),
  h2 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp
  ),
  h3 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 19.sp
  ),
  h4 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp
  ),
  body1 = TextStyle(
    fontWeight = FontWeight.Light,
    fontSize = 16.sp
  ),
  body2 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp
  ),
  subtitle1 = TextStyle(
    fontWeight = FontWeight.Light,
    fontSize = 12.sp
  ),
  subtitle2 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp
  ),
  caption = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp
  ),
)

val TimeScreenTypography = Typography(
  h1 = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Light,
  ),
  h2 = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.SemiBold,
  ),
  body1 = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
  ),
  subtitle1 = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.Light,
    letterSpacing = 0.75.sp
  ),
)

val FindWorkspaceScreenTypography = Typography(
  h6 = TextStyle(
    fontSize = 20.sp,
    color = Color.White.copy(0.5f)
  ),
  subtitle1 = TextStyle(
    fontSize = 14.sp,
  ),
  caption = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.Light,
  ),
)