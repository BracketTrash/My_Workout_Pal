package ie.wit.myworkoutpal.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoutineModel(var id: Long = 0,
                        var title: String = "",
                        var description: String = "",
                        var sets: String = "") : Parcelable

