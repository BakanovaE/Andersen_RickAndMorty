package org.martellina.rickandmorty.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersFilter (
    var name: String? = null,
    var status: String? = null,
    var species: String? = null,
    var type: String? = null,
    var gender: String? = null
): Parcelable