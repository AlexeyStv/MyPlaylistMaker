package com.practicum.android.playlistmaker.trackinfo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.android.playlistmaker.R
import com.practicum.android.playlistmaker.SearchActivity
import com.practicum.android.playlistmaker.classes.Track
import java.text.SimpleDateFormat
import java.util.Locale

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
        private const val REFRESH_LIST_DELAY_MILLIS = 250L

        //playerState
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    //
    private lateinit var play: ImageView

    //private lateinit var songTimer: TextView
    private var mediaPlayer = MediaPlayer()
    lateinit var handler: Handler
    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track2)
        supportActionBar?.hide()

        track = if (savedInstanceState != null)
            savedInstanceState.getParcelable(ONE_TRACK)!!
        else
            intent.getParcelableExtra(SearchActivity.TRACK_DATA)!!

        initUI()
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

        play = findViewById(R.id.ivPlay)
        handler = Handler(Looper.getMainLooper())
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
    }

    private fun showData(track: Track) {
        val radius = resources.getDimensionPixelOffset(R.dimen.at_radius_Art)

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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
        }
    }

    private fun UpdateTimer() {

        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (playerState == STATE_PLAYING) {
                        getTime()
                        handler.postDelayed(this, REFRESH_LIST_DELAY_MILLIS)
                    }
                    if (playerState == STATE_PREPARED)
                        tvTimer.text = resources.getString(R.string.at_empty_timer)
                    else
                        getTime()
                }
            }, REFRESH_LIST_DELAY_MILLIS
        )
    }

    private fun getTime() {
        tvTimer.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING

        UpdateTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PAUSED, STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}