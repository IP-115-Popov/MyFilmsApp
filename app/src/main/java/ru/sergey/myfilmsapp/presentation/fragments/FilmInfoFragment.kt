package ru.sergey.myfilmsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.getActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.sergey.myfilmsapp.databinding.FragmentFilmInfoBinding
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel
import ru.sergey.myfilmsapp.Сonstant

class FilmInfoFragment() : Fragment() {
    private var _binding: FragmentFilmInfoBinding? = null
    private val binding: FragmentFilmInfoBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentFilmInfo must not be null")

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = getActivityViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm: MainViewModel by activityViewModel<MainViewModel>()
        val filmId = arguments?.getLong(Сonstant.FILM_ID)
        val film = vm.films.value.find { it.id == filmId }
        if (film != null) {
        binding.apply {
            //moviePoster
            movieTitle.text = film.localized_name
            movieGenre.text = film.genres.toString()
            tvRating.text = film.rating.toString()
            movieDescription.text = film.description

        }
            }
    }
}