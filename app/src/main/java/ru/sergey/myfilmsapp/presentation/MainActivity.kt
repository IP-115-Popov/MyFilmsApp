package ru.sergey.myfilmsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.databinding.ActivityMainBinding
import ru.sergey.myfilmsapp.presentation.fragments.FilmsListFragment
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityMain must not be null")

    private val vm by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            openFragment(FilmsListFragment(),R.id.fragment_container)
        }
    }
    fun openFragment(fragment: Fragment, idHolder : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(idHolder, fragment)
            .commit()
    }
}