package org.martellina.rickandmorty.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.martellina.rickandmorty.R
import org.martellina.rickandmorty.databinding.ActivityMainBinding
import org.martellina.rickandmorty.ui.fragments.FragmentCharacters
import org.martellina.rickandmorty.ui.fragments.FragmentEpisodes
import org.martellina.rickandmorty.ui.fragments.FragmentLocations


class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RickAndMorty)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().run {
                replace(R.id.nav_host_fragment, FragmentCharacters.newInstance())
                commit()
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.characters -> navigate(FragmentCharacters.newInstance())
                R.id.episodes -> navigate(FragmentEpisodes.newInstance())
                R.id.locations -> navigate(FragmentLocations.newInstance())
            }
            true
        }
    }

    override fun navigate(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.nav_host_fragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun goBack() {
        onBackPressed()
    }

    private fun isConnected(): Boolean? {
        val connectivityManager = applicationContext.getSystemService( Context.CONNECTIVITY_SERVICE ) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected
    }
}