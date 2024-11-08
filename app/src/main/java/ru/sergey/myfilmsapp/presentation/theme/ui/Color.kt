package ru.sergey.myfilmsapp.presentation.theme.ui

import androidx.compose.ui.graphics.Color

val PrimaryColor = hexToColor("#0E3165")
val White = hexToColor("#FFFFFFFF")
val SelectedGenreBackground = hexToColor("#FFC967")
val DialogBackground = hexToColor("#232323")

val BackgroundColor = White

fun hexToColor(hex: String) = Color(android.graphics.Color.parseColor(hex))