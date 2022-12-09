package com.practicum.android.playlistmaker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.android.playlistmaker.api.ITunesApi
import com.practicum.android.playlistmaker.api.ITunesResponse
import com.practicum.android.playlistmaker.classes.SState
import com.practicum.android.playlistmaker.classes.StateEmptyText
import com.practicum.android.playlistmaker.classes.StateEmptyTracks
import com.practicum.android.playlistmaker.classes.StateGoodResult
import com.practicum.android.playlistmaker.classes.StateNoConnection
import com.practicum.android.playlistmaker.classes.StateNullResult
import com.practicum.android.playlistmaker.classes.StateServerError
import com.practicum.android.playlistmaker.classes.Track
import com.practicum.android.playlistmaker.classes.TracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    //UI RecyclerView Data
    private var tracks: ArrayList<Track> = ArrayList()
    private var adapterTr: TracksAdapter = TracksAdapter(tracks)
    private lateinit var recView: RecyclerView

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val CURRENT_STATE = "CURRENT_STATE"
        const val TRACK_LIST = "iTrackList"
    }

    //UI
    private lateinit var etSearch: EditText
    private lateinit var ivClearText: ImageView

    //UI Message
    private lateinit var layoutMessage: LinearLayout
    private lateinit var ivIcon: ImageView
    private lateinit var tvMainMessage: TextView
    private lateinit var tvDescMessage: TextView
    private lateinit var btUpdate: Button

    //
    private var searchState: SState? = StateNullResult()

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ITunesApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        initUI()
        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT, "").toString()
            searchState = savedInstanceState.getParcelable(CURRENT_STATE)
            val previousTracks: ArrayList<Track> =
                savedInstanceState.getParcelableArrayList(TRACK_LIST)!!
            showData(searchText, previousTracks)
        } else {
            hideMessage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, etSearch.text.toString())
        outState.putParcelable(CURRENT_STATE, searchState)
        outState.putParcelableArrayList(TRACK_LIST, tracks)
    }

    //--------------------------------------------------//
    private fun initUI() {
        val tvBack = findViewById<TextView>(R.id.tvBack)
        tvBack.setOnClickListener { finish() }
        etSearch = findViewById(R.id.etSearch)
        ivClearText = findViewById(R.id.ivClearText)
        recView = findViewById(R.id.rvDataTracks)
        layoutMessage = findViewById(R.id.LinearLayout_BadData)
        ivIcon = findViewById(R.id.icon_message)
        tvMainMessage = findViewById(R.id.mainMessage)
        tvDescMessage = findViewById(R.id.descriptionMessage)
        btUpdate = findViewById(R.id.btnUpdate)

        initEvent()
    }

    private fun initEvent() {
        adapterTr = TracksAdapter(tracks)
        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recView.adapter = adapterTr

        ivClearText.setOnClickListener { clearSearchData() }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    ivClearText.visibility = View.INVISIBLE
                    clearRecyclerView()
                } else
                    ivClearText.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        etSearch.addTextChangedListener(simpleTextWatcher)
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ //true
                downloadData()
            }
            false
        }

        btUpdate.setOnClickListener { downloadData() }
    }

    private fun showData(searchTxt: String, prevTracks: ArrayList<Track>) {

        if (searchTxt.trim().isNotEmpty())
            etSearch.setText(searchTxt)

        when (searchState) {
            is StateNullResult -> hideMessage()
            is StateGoodResult -> {
                hideMessage()
                showItunesData(prevTracks)
            }
            else -> showInfo(searchState)
        }
    }

    //region Button event
    private fun clearSearchData() {
        etSearch.setText("")
        ivClearText.visibility = View.INVISIBLE

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = this.currentFocus
        inputMethodManager?.hideSoftInputFromWindow(currentView?.windowToken, 0)
    }

    private fun downloadData() {
        if (checkQueryInput()) {
            if (isInternet(this)) {
                getITunesData()
            } else {
                searchState = StateNoConnection()
                showInfo(searchState)
            }
        } else {
            searchState = StateEmptyText()
            showInfo(searchState)
        }
    }
    //endregion

    //
    private fun checkQueryInput(): Boolean {
        return etSearch.text.toString().trim().isNotEmpty()
    }

    private fun isInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    private fun showInfo(state: SState?){

        val mainMessage: String
        val mainMessageDescribe: String
        val imageR: Int

        when (state) {
            is StateEmptyText ->
            {
                mainMessage = resources.getString(R.string.message_empty_query)
                mainMessageDescribe = resources.getString(R.string.message_empty_descr)
                imageR = R.drawable.ic_no_data
            }
            //NO_CONNECTION ->
            is StateNoConnection ->
            {
                mainMessage = resources.getString(R.string.message_no_connection)
                mainMessageDescribe = resources.getString(R.string.message_no_connection_desc)
                imageR = R.drawable.ic_no_internet
            }

            is StateEmptyTracks ->
            //EMPTY_TRACKS_DATA ->
            {
                mainMessage = resources.getString(R.string.message_nothing_show)
                mainMessageDescribe = ""
                imageR = R.drawable.ic_no_data
            }
            is StateServerError ->
            //SERVER_ERROR ->
            {
                mainMessage = resources.getString(R.string.message_server_error)
                mainMessageDescribe = ""
                imageR = R.drawable.ic_no_data
            }
            else -> {
                mainMessage = resources.getString(R.string.message_app_error)
                mainMessageDescribe = resources.getString(R.string.message_app_error_descr)
                imageR = R.drawable.ic_delete_txt
            }
        }

        showMessage(mainMessage, mainMessageDescribe, imageR)
    }

    private fun getITunesData() {
        hideMessage()
        gettingData()
    }

    private fun gettingData() {

        val search = etSearch.text.toString()
        itunesService.search(search).enqueue(object : Callback<ITunesResponse> {

            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            val newTracks: ArrayList<Track> = response.body()?.results!!
                            searchState = StateGoodResult()
                            showItunesData(newTracks)
                        } else {
                            searchState = StateEmptyTracks()
                            showInfo(searchState)
                        }
                    }
                    else -> {
                        searchState = StateServerError()
                        showInfo(searchState)
                    }
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                searchState = StateServerError()
                showInfo(searchState)
            }
        })
    }

    //
    private fun showMessage(message: String, description: String, imageR: Int) {
        ivIcon.setImageResource(imageR)
        tvMainMessage.text = message
        tvDescMessage.text = description
        hideItunesData()
        layoutMessage.visibility = View.VISIBLE
    }

    private fun hideMessage() {
        ivIcon.setImageResource(R.drawable.ic_no_image)
        tvMainMessage.text = ""
        tvDescMessage.text = ""
        layoutMessage.visibility = View.INVISIBLE
    }

    private fun clearRecyclerView() {
        val emptyTracks = arrayListOf<Track>()
        showItunesData(emptyTracks)
    }

    private fun hideItunesData() {
        clearRecyclerView()
        recView.visibility = View.INVISIBLE
    }

    private fun showItunesData(newTrack: ArrayList<Track>) {
        recView.visibility = View.VISIBLE
        tracks.clear()
        tracks.addAll(newTrack)
        adapterTr.notifyDataSetChanged()
    }
}