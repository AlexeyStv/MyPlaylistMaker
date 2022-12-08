package com.practicum.android.playlistmaker.classes

object TestData {
    fun getTracks(): ArrayList<Track> {
        val tracks: ArrayList<Track> = arrayListOf()
        tracks.add(
            Track(
                "Smells Like Teen Spirit", "Nirvana", 300000L,
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
                //"https://is5-ssl.mzstatic.com/image/something.jpg" //тест пустого изображения
            )
        )
        tracks.add(
            Track(
                "Billie Jean", "Michael Jackson", 250000L,
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Stayin' Alive", "Bee Gees", 240000L,
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Whole Lotta Love", "Led Zeppelin", 320000L,
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Sweet Child O'Mine", "Guns N' Roses", 311100L,
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
        return tracks
    }
}