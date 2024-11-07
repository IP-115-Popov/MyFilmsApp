package ru.sergey.myfilmsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.sergey.myfilmsapp.databinding.FragmentFilmInfoBinding
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel
import ru.sergey.myfilmsapp.Сonstant

class FilmInfoFragment() : Fragment() {
    private var _binding: FragmentFilmInfoBinding? = null
    private val binding: FragmentFilmInfoBinding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentFilmInfo must not be null")

    private val vm: MainViewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filmId = arguments?.getLong(Сonstant.FILM_ID)
        val film = vm.items.value?.find { it.id == filmId } ?: return
        binding.apply {
            tvTitleInfo.text = film.name
        }
    }
}