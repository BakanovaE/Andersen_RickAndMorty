package org.martellina.rickandmorty.ui

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigate(fragment: Fragment)
    fun goBack()
}