package org.martellina.rickandmorty.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodesFilter(
    var name: String? = null,
    var code: String? = null
): Parcelable
