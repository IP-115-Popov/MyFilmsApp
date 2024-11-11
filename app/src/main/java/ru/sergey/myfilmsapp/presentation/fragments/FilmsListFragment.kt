package ru.sergey.myfilmsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.fragment.app.Fragment

import org.koin.androidx.viewmodel.ext.android.getActivityViewModel

import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.databinding.FragmentFilmsListBinding
import ru.sergey.myfilmsapp.presentation.compose.FilmListScreen

import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel
import ru.sergey.myfilmsapp.Сonstant

class FilmsListFragment : Fragment() {
    private var _binding: FragmentFilmsListBinding? = null
    private val binding: FragmentFilmsListBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFilmsList must not be null")

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = getActivityViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsListBinding.inflate(inflater)
        return binding.root
    }

    fun onFilmClick(id: Long) {
        val bundle = Bundle()
        bundle.putLong(Сonstant.FILM_ID, id)

        val secondFragment = FilmInfoFragment()
        secondFragment.arguments = bundle

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentFilmListCompose.setContent @OptIn(ExperimentalLayoutApi::class) {
            FilmListScreen(vm, ::onFilmClick)
        }
    }

}