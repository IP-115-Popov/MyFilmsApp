package ru.sergey.myfilmsapp.presentation.theme.ui

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PrimaryColor = hexToColor("#0E3165")

val Black = hexToColor("#FF000000")
val White = hexToColor("#FFFFFFFF")
val PrimaryColorDark = hexToColor("#00574B")
val ColorAccent = hexToColor("#D81B60")
val SelectedGenreBackground = hexToColor("#FFC967")
val DialogBackground = hexToColor("#232323")

fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}