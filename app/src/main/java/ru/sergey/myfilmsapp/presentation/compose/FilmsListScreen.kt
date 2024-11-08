package ru.sergey.myfilmsapp.presentation.compose

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(vm : MainViewModel) {
    val genres = remember { vm.genres }
    val films = vm.films.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Фильмы") })
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Жанры",
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(genres) { genre ->
                GenreItem(genre)
            }

            item {
                Text(
                    text = "Фильмы",
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(films.value) { film ->
                FilmItem(film)
            }
        }
    }
}

@Composable
fun GenreItem(genre: String) {
    Text(
        text = genre,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                // Обработка клика по жанру
            }
    )
}

@Composable
fun FilmItem(film: Film) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Здесь вы можете добавить изображение постера фильма
            Text(text = film.localized_name)
            // Добавьте другие детали фильма (например, жанр, год и т. д.)
        }
    }
}
