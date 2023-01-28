package com.practicum.android.playlistmaker.trackinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.android.playlistmaker.R
import com.practicum.android.playlistmaker.SearchActivity
import com.practicum.android.playlistmaker.classes.Track

class TrackActivity : AppCompatActivity() {

    private lateinit var track: Track

    //UI
    private lateinit var tvTrackImage: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvBand: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvDurationTrackData: TextView
    private lateinit var tvAlbumData: TextView
    private lateinit var tvYearData: TextView
    private lateinit var tvGenreData: TextView
    private lateinit var tvCountryData: TextView

    private lateinit var buttonGroup: Group

    private companion object {
        const val ONE_TRACK = "one_track_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track2)
        supportActionBar?.hide()

        initUI()

        track = if (savedInstanceState != null)
            savedInstanceState.getParcelable(ONE_TRACK)!!
        else
            intent.getParcelableExtra(SearchActivity.TRACK_DATA)!!

        showData(track)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ONE_TRACK, track)
    }

    private fun initUI() {
        val tvBack = findViewById<TextView>(R.id.ivBack)
        tvBack.setOnClickListener { finish() }

        buttonGroup = findViewById(R.id.buttonGroup)
        tvTrackImage = findViewById(R.id.ivTrackImage)
        tvTrackName = findViewById(R.id.tvTrackName)
        tvBand = findViewById(R.id.tvBand)
        tvTimer = findViewById(R.id.tvTimer)
        tvDurationTrackData = findViewById(R.id.tvDurationTrack_data)
        tvAlbumData = findViewById(R.id.tvAlbum_data)
        tvYearData = findViewById(R.id.tvYear_data)
        tvGenreData = findViewById(R.id.tvGenre_data)
        tvCountryData = findViewById(R.id.tvCountry_data)
    }

    private fun showData(track: Track) {
        var radius = resources.getDimensionPixelOffset(R.dimen.at_radius_Art)

        Glide.with(this)
            .load(track.getArtworkUrl512())
            .placeholder(R.drawable.ic_no_track_image)
            .fitCenter()
            .transform(RoundedCorners(radius))
            .into(tvTrackImage)

        track.trackName?.let { tvTrackName.text = it } ?: run {
            tvTrackName.text = resources.getString(R.string.at_no_data)
        }

        track.artistName?.let { tvBand.text = it } ?: run {
            tvBand.text = resources.getString(R.string.at_no_data)
        }

        tvTimer.text = resources.getString(R.string.at_empty_timer)
        track.trackTimeMillis.let { tvDurationTrackData.text = track.getDuration() }

        track.collectionName?.let { tvAlbumData.text = track.collectionName } ?: run {
            tvAlbumData.text = resources.getString(R.string.at_no_data)
            buttonGroup.visibility = View.GONE
        }

        track.releaseDate?.let { tvYearData.text = it.substring(0, 4) } ?: run {
            tvYearData.text = resources.getString(R.string.at_no_data)
        }

        track.primaryGenreName?.let { tvGenreData.text = it } ?: run {
            tvGenreData.text = resources.getString(R.string.at_no_data)
        }

        track.country?.let { tvCountryData.text = it } ?: run {
            tvCountryData.text = resources.getString(R.string.at_no_data)
        }
    }
}