package ru.sergey.myfilmsapp.presentation.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import ru.sergey.myfilmsapp.presentation.theme.ui.BackgroundColor
import ru.sergey.myfilmsapp.presentation.theme.ui.MyBlack
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
    val selectedGenre = vm.selectedGenre.collectAsState()//rememberSaveable { mutableStateOf<String?>(null) }
    val isLoadingFailed = vm.isLoadingFailed.collectAsState().value  // Получаем состояние ошибки
    val isLoading = vm.isLoading.collectAsState().value  // Получаем состояние ошибки

    Log.i("FilmListScreen", "Films: ${films.value.size}, isLoadingFailed: $isLoadingFailed")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Text(
                            text = stringResource(R.string.Films),
                            color = White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight(500),
                            letterSpacing = 0.15.sp,
                            lineHeight = 22.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor, titleContentColor = White
                ),
                modifier = Modifier.height(56.dp)
            )
        },
    ) { innerPadding ->
        val startPadding = PaddingValues(start = 16.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        )
        if (isLoading && !isLoadingFailed) {
            //Загрузка
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
        } else if (isLoadingFailed) {
            //Ошибка
            ErrorDialog(onRetry = {
                vm.getFilms()
            })
        } else {
            LazyColumn(
                contentPadding = innerPadding, modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier.padding(top = 8.dp).height(40.dp).fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.Genres),
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.1.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(startPadding),
                            lineHeight = 22.sp,
                            color = MyBlack

                        )
                    }
                }
                items(genres) { genre ->
                    GenreItem(genre,
                        modifier = Modifier.padding(startPadding),
                        selectedGenre
                    ) {
                        if (selectedGenre.value == genre) {
                            vm.setSelectedGenre(null)
                        } else {
                            vm.setSelectedGenre(genre)
                        }
                        vm.getFilms(vm.selectedGenre.value)
                    }
                }
                item {
                    Box(
                        modifier = Modifier.padding(top = 16.dp).height(40.dp).fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.Films),
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.1.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            lineHeight = 22.sp,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(startPadding),
                            color = MyBlack
                        )
                    }
                }
                items(films.value.chunked(2)) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(startPadding)
                            .padding(top = 8.dp, bottom =  8.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        if (row.getOrNull(0) != null) FilmCard(
                            row[0],
                            Modifier.fillMaxWidth(0.5f)
                                .padding( end = 4.dp),
                            onFilmSelected
                        )
                        if (row.getOrNull(1) != null) FilmCard(
                            row[1],
                            Modifier.fillMaxWidth()
                                .padding(start = 4.dp),
                            onFilmSelected
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
    selectedGenre: State<String?>,
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
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontWeight = FontWeight(400),
            letterSpacing = 0.1.sp,
            lineHeight = 20.sp,
            color = MyBlack
        )
    }
}

@Composable
fun FilmCard(film: Film, modifier: Modifier = Modifier, onFilmSelected: (Long) -> Unit) {
    val defaultImage = R.drawable.img_not_find
    Surface(modifier = modifier
        .clickable {
            onFilmSelected(film.id)
        }) {
        Column(Modifier.background(BackgroundColor)) {
            AsyncImage(
                model = film.image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .height(260.dp)
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
                modifier = Modifier.size(160.dp, 48.dp).padding(top = 8.dp),
                fontWeight = FontWeight(700),
                letterSpacing = 0.1.sp,
                color = MyBlack
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
                .padding(8.dp)
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .background(DialogBackground)
                .padding(top = 18.dp, start = 16.dp, end = 16.dp, bottom = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.NetworkConnectionError),
                color = White,
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontWeight = FontWeight(400),
                lineHeight = 20.sp
            )
            Text(text = stringResource(R.string.Repeat),
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.End,
                lineHeight = 16.sp,
                color = SelectedGenreBackground,
                modifier = Modifier.clickable {
                    onRetry()
                })
        }
    }
}