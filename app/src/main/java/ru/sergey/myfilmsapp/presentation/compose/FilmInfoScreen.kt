package ru.sergey.myfilmsapp.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.presentation.theme.ui.MyBlack
import ru.sergey.myfilmsapp.presentation.theme.ui.GreyFont
import ru.sergey.myfilmsapp.presentation.theme.ui.PrimaryColor
import ru.sergey.myfilmsapp.presentation.theme.ui.White
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmInfoScreen(film: Film, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Text(
                            text = if (film.name == null || film.name == "") stringResource(R.string.default_film_name) else film.name,
                            color = White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight(500),
                            letterSpacing = 0.15.sp,
                            lineHeight = 22.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.Back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                modifier = Modifier.height(56.dp)
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // Отображаем постер фильма с проверкой на пустой URL
            MoviePoster(film.image_url, modifier = Modifier.padding(top = 24.dp))

            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(32.dp),
                text = if (film.localized_name == null || film.localized_name == "")
                            stringResource(R.string.default_localized_name)
                       else film.localized_name,
                fontSize = 26.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                letterSpacing = 0.1.sp,
                lineHeight = 32.sp,
                color = MyBlack
            )

            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(20.dp),
                text = (film.genres.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "")

                        + ", " + (film.year.takeIf { it > 0 }?.toString() ?: "???") + stringResource(R.string.year),
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                letterSpacing = 0.1.sp,
                color = GreyFont,
                lineHeight = 20.sp
            )

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val roundedRating = BigDecimal( film.rating ).setScale(1, RoundingMode.HALF_UP)

                Text(
                    text = roundedRating.takeIf { it > BigDecimal.ZERO }?.toString() ?: "???", // Проверка на пустой рейтинг
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontWeight = FontWeight(700),
                    color = PrimaryColor,
                    letterSpacing = 0.1.sp,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "КиноПоиск",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    color = PrimaryColor,
                    letterSpacing = 0.1.sp,
                    lineHeight = 16.sp
                )
            }

            Text(
                modifier = Modifier.padding(top = 14.dp),
                text = if (film.description == null) "" else film.description,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Justify,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                letterSpacing = 0.1.sp,
                lineHeight = 20.sp,
                color = MyBlack
            )
        }
    }
}

@Composable
fun MoviePoster(url: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = if (url.isNotEmpty()) url else R.drawable.img_not_find, // Использование заглушки на случай пустого URL
            contentDescription = null,
            modifier = Modifier
                .size(132.dp, 201.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(White),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.img_not_find),
            placeholder = painterResource(id = R.drawable.img_not_find)
        )
    }
}
