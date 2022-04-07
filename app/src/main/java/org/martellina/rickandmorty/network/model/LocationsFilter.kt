package org.martellina.rickandmorty.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationsFilter (
    var name: String? = null,
    var type: String? = null,
    var dimension: String? = null
): Parcelable