package com.practicum.android.playlistmaker.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface SearchState {}

@Parcelize
sealed class SState() : SearchState, Parcelable {}

class StateNullResult() : SState()
class StateGoodResult() : SState()
class StateEmptyText() : SState()
class StateNoConnection() : SState()
class StateEmptyTracks() : SState()
class StateServerError() : SState()