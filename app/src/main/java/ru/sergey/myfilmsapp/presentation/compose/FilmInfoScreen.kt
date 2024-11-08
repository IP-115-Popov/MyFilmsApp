package ru.sergey.myfilmsapp.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.presentation.theme.ui.PrimaryColor
import ru.sergey.myfilmsapp.presentation.theme.ui.White
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmInfoScreen(vm : MainViewModel, film : Film, onBackClick : ()->Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = film.name,
                            modifier = Modifier.align(Alignment.Center),
                            color = White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = White,
                    navigationIconContentColor = White)
            )
        }
    ) {  innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
fun MoviePoster(url : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MovieTitleAndInfo(film: Film) {
    Column {
        Text(
            text = film.localized_name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = film.genres.joinToString(", ") + ", " + film.year + " год", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = film.rating.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "КиноПоиск", fontSize = 16.sp)
        }
    }
}

@Composable
fun MovieSynopsis(film : Film) {
    Text(
        text = film.description,
        textAlign = TextAlign.Justify
    )
}
