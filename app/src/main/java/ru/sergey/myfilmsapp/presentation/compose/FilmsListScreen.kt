package ru.sergey.myfilmsapp.presentation.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.presentation.theme.ui.BackgroundColor
import ru.sergey.myfilmsapp.presentation.theme.ui.DialogBackground
import ru.sergey.myfilmsapp.presentation.theme.ui.PrimaryColor
import ru.sergey.myfilmsapp.presentation.theme.ui.SelectedGenreBackground
import ru.sergey.myfilmsapp.presentation.theme.ui.White
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    vm: MainViewModel, onFilmSelected: (Long) -> Unit
) {
    val genres = remember { vm.genres }
    val films = vm.films.collectAsState()
    val selectedGenre = rememberSaveable { mutableStateOf<String?>(null) }
    val isLoadingFailed = vm.isLoadingFailed.collectAsState().value  // Получаем состояние ошибки
    val isLoading = vm.isLoading.collectAsState().value  // Получаем состояние ошибки

    Log.i("FilmListScreen", "Films: ${films.value.size}, isLoadingFailed: $isLoadingFailed")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.Films),
                        color = White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_bold))
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor, titleContentColor = White
                ),
            )
        },
    ) { innerPadding ->
        val startPadding = PaddingValues(start = 12.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        )
        if (isLoading == true && isLoadingFailed == false) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = SelectedGenreBackground,
                )
            }
        } else if (isLoadingFailed == true) {
            ErrorDialog(onRetry = {
                vm.getFilms()
            })
        } else {
            LazyColumn(
                contentPadding = innerPadding, modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier.size(360.dp, 40.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Genres),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(startPadding)
                        )
                    }
                }
                items(genres) { genre ->
                    GenreItem(genre,
                        modifier = Modifier.padding(startPadding),
                        selectedGenre,
                        {
                            if (selectedGenre.value == genre) {
                                selectedGenre.value = null
                            } else {
                                selectedGenre.value = genre
                            }
                            vm.getFilms(selectedGenre.value)
                        })
                }
                item {
                    Box(
                        modifier = Modifier.size(360.dp, 40.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Films),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(startPadding)
                        )
                    }
                }
                items(films.value.chunked(2)) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(startPadding)
                    ) {
                        if (row.getOrNull(0) != null) FilmCard(
                            row[0], Modifier.fillMaxWidth(0.5f), onFilmSelected
                        )
                        if (row.getOrNull(1) != null) FilmCard(
                            row[1], Modifier.fillMaxWidth(), onFilmSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreItem(
    genre: String,
    modifier: Modifier,
    selectedGenre: MutableState<String?>,
    onClick: (String) -> Unit
) {
    val defaultModifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .background(
            if (selectedGenre.value == genre)
                SelectedGenreBackground
            else BackgroundColor
        )
        .clickable {
            onClick(genre)
        }
    Box(
        modifier = defaultModifier.then(modifier)

    ) {
        Text(
            text = genre,
            modifier = Modifier.align(Alignment.CenterStart),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.roboto_regular))
        )
    }
}

@Composable
fun FilmCard(film: Film, modifier: Modifier = Modifier, onFilmSelected: (Long) -> Unit) {
    val defaultImage = R.drawable.img_not_find
    Surface(modifier = modifier
        .padding(5.dp)
        .clickable {
            onFilmSelected(film.id)
        }) {
        Column(Modifier.background(BackgroundColor)) {
            AsyncImage(
                model = film.image_url,
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp, 222.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = defaultImage)
            )
            Text(
                text = film.localized_name,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.size(160.dp, 40.dp)
            )
        }
    }
}

@Composable
fun ErrorDialog(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .size(344.dp, 56.dp)
                .align(Alignment.BottomCenter)
                .background(DialogBackground)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.NetworkConnectionError),
                color = White,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
            Text(text = stringResource(R.string.Repeat),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                color = SelectedGenreBackground,
                modifier = Modifier.clickable {
                    onRetry()
                })
        }
    }
}
