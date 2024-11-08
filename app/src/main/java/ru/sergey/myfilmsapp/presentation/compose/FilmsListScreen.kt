package ru.sergey.myfilmsapp.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.presentation.theme.ui.PrimaryColor
import ru.sergey.myfilmsapp.presentation.theme.ui.SelectedGenreBackground
import ru.sergey.myfilmsapp.presentation.theme.ui.White
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(vm : MainViewModel,onGenreSelected : (String)->Unit, onFilmSelected : (Long)->Unit ) {
    val genres = remember { vm.genres }
    val films = vm.films.collectAsState()
    var selectedGenre = remember { mutableStateOf<String?>(null) }
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
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.size(360.dp, 40.dp)
                ) {
                    Text(
                        text = "Жанры",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
            items(genres) { genre ->
                GenreItem(
                    genre,
                    onGenreSelected,
                    selectedGenre,
                    { genre ->
                        selectedGenre.value = genre
                    })
            }
            item {
                Box(
                    modifier = Modifier.size(360.dp, 40.dp)
                ) {
                    Text(
                        text = "Фильмы",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
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
fun GenreItem(genre: String, onGenreSelected : (String)->Unit, selectedGenre : MutableState<String?>, onClick : (String)->Unit) {
    Box(
        modifier = Modifier.size(360.dp, 40.dp)
            .background( if (selectedGenre.value == genre) SelectedGenreBackground else White)
            .clickable {
            onGenreSelected(genre)
            onClick(genre)
        }

    ) {
        Text(
            text = genre,
            modifier = Modifier
                .align(Alignment.CenterStart)
        )
    }
}

@Composable
fun FilmCard(film: Film, modifier: Modifier = Modifier, onFilmSelected : (Long)->Unit) {
    val defaultImage = R.drawable.img_not_find
    Surface(modifier = modifier
        .padding(5.dp)
        .clickable {
            onFilmSelected(film.id)
        }
    ) {
        Column {
            AsyncImage(
                model = film.image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = defaultImage)
            )
            Text(
                text = film.localized_name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}