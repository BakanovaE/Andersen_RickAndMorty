package org.martellina.rickandmorty.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterLocation(
    val name: String,
    val url: String
) : Parcelable