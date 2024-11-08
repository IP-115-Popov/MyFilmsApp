package ru.sergey.myfilmsapp.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.presentation.theme.ui.PrimaryColor
import ru.sergey.myfilmsapp.presentation.theme.ui.White
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(vm : MainViewModel,onGenreSelected : (String)->Unit, onFilmSelected : (Long)->Unit ) {
    val genres = remember { vm.genres }
    val films = vm.films.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Фильмы",
                            modifier = Modifier.align(Alignment.Center),
                            color = White
                        )
                    }
                },
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = White),
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Жанры",
                    modifier = Modifier.padding(16.dp),
                    fontWeight= FontWeight.Bold
                )
            }
            items(genres) { genre ->
                GenreItem(genre,onGenreSelected)
            }
            item {
                Text(
                    text = "Фильмы",
                    modifier = Modifier.padding(16.dp),
                    fontWeight= FontWeight.Bold
                )
            }
            items(films.value.chunked(2)) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (row.getOrNull(0) != null) FilmCard(row[0], Modifier.fillMaxWidth(0.5f), onFilmSelected)
                    if (row.getOrNull(1) != null) FilmCard(row[1], Modifier.fillMaxWidth(), onFilmSelected)
                }
            }
        }
    }
}
@Composable
fun GenreItem(genre: String, onGenreSelected : (String)->Unit) {
    Text(
        text = genre,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onGenreSelected(genre)
            }
    )
}

@Composable
fun FilmCard(film: Film, modifier: Modifier = Modifier, onFilmSelected : (Long)->Unit) {
    Card(modifier = modifier
        .clickable {
            onFilmSelected(film.id)
        }
    ) {
        Column {
            AsyncImage(
                model = film.image_url,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
            Text(film.localized_name)

        }
    }
}