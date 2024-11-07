package ru.sergey.myfilmsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.databinding.FragmentFilmsListBinding
import ru.sergey.myfilmsapp.presentation.adapters.FilmAdapter
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel
import ru.sergey.myfilmsapp.Сonstant

class FilmsListFragment : Fragment() {
    private var _binding: FragmentFilmsListBinding? = null
    private val binding: FragmentFilmsListBinding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFilmsList must not be null")

    private val vm: MainViewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmsListBinding.inflate(inflater)
        return binding.root
    }

    fun onItemClick(id: Long) {
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
        binding.rv.layoutManager = GridLayoutManager(context, 2)
        binding.rv.adapter = FilmAdapter(emptyList(), ::onItemClick) // Создаем адаптер с пустым списком
        vm.items.observe(viewLifecycleOwner) { films ->
            (binding.rv.adapter as FilmAdapter).replaceAllFilm(films) // Обновляем адаптер, когда данные будут доступны
        }
    }

}