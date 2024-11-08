package ru.sergey.myfilmsapp.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.R
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
                    Text(
                        text = film.name,
                        color = White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_bold))
                    )
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
                )
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
                .padding(15.dp)
        ) {
            MoviePoster(film.image_url)
            Spacer(modifier = Modifier.height(16.dp))
            MovieTitleAndInfo(film)
            Spacer(modifier = Modifier.height(16.dp))
            MovieSynopsis(film)
        }
    }
}

@Composable
fun MoviePoster(url: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .size(132.dp, 201.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(White),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.img_not_find)
        )
    }
}

@Composable
fun MovieTitleAndInfo(film: Film) {
    Column {
        Text(
            text = film.localized_name,
            fontSize = 26.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = film.genres.joinToString(", ") + ", " + film.year + stringResource(R.string.year),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val roundedRating = BigDecimal(film.rating).setScale(1, RoundingMode.HALF_UP)
            Text(
                text = roundedRating.toString(),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                color = PrimaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "КиноПоиск",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        }
    }
}

@Composable
fun MovieSynopsis(film: Film) {
    Text(
        text = film.description,
        textAlign = TextAlign.Justify,
        fontFamily = FontFamily(Font(R.font.roboto_regular))
    )
}
