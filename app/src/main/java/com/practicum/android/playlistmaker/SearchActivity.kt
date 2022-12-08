package com.practicum.android.playlistmaker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.android.playlistmaker.api.ITunesApi
import com.practicum.android.playlistmaker.api.ITunesResponse
import com.practicum.android.playlistmaker.classes.TestData
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

        const val NULL_RESULT = 0
        const val GOOD_RESULT = 1

        const val EMPTY_TEXT = 10
        const val NO_CONNECTION = 20
        const val EMPTY_TRACKS_DATA = 30
        const val SERVER_ERROR = 40
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
    private var currentState: Int = NULL_RESULT
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

        var searchText = ""
        if (savedInstanceState != null)
        {
            searchText = savedInstanceState.getString(SEARCH_TEXT, "").toString()
            currentState = savedInstanceState.getInt(CURRENT_STATE, NULL_RESULT)
            tracks = savedInstanceState.getParcelableArrayList(TRACK_LIST)!!
        }

        initUI()
        showData(searchText)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, etSearch.text.toString())
        outState.putInt(CURRENT_STATE, currentState)
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
    private fun initEvent(){
        ivClearText.setOnClickListener { clearSearchData() }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    ivClearText.visibility = View.INVISIBLE
                    clearRecyclerView()
                }
                else
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
    private fun showData(searchTxt: String){

        if(searchTxt.trim().isNotEmpty())
            etSearch.setText(searchTxt)

        when(currentState)
        {
            NULL_RESULT -> hideMessage()
            GOOD_RESULT ->
            {
                hideMessage()
                showItunesData()
            }
            else -> showInfo(currentState)
        }
    }

    //region Button event
    private fun clearSearchData() {
        etSearch.setText("")
        ivClearText.visibility = View.INVISIBLE

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = this.currentFocus //this.getCurrentFocus()
        inputMethodManager?.hideSoftInputFromWindow(currentView?.windowToken, 0)
    }
    private fun downloadData(){
        if(checkQueryInput())
        {
            if(isInternet(this))
            {
                getITunesData()
            }
            else
            {
                currentState = NO_CONNECTION
                showInfo(NO_CONNECTION)
            }
        }
        else
        {
            currentState = EMPTY_TEXT
            showInfo(EMPTY_TEXT)
        }
    }
    //endregion

    //
    private fun checkQueryInput() : Boolean {
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
    private fun showInfo(state: Int){

        val mainMessage: String
        val mainMessageDescr: String
        val imageR: Int

        when(state)
        {
            EMPTY_TEXT ->
            {
                mainMessage = resources.getString(R.string.message_empty_query)
                mainMessageDescr = resources.getString(R.string.message_empty_descr)
                imageR = R.drawable.ic_no_data
            }
            NO_CONNECTION ->
            {
                mainMessage = resources.getString(R.string.message_no_connection)
                mainMessageDescr = resources.getString(R.string.message_no_connection_desc)
                imageR = R.drawable.ic_no_internet
            }

            EMPTY_TRACKS_DATA ->
            {
                mainMessage = resources.getString(R.string.message_nothing_show)
                mainMessageDescr = ""
                imageR = R.drawable.ic_no_data
            }
            SERVER_ERROR ->
            {
                mainMessage = resources.getString(R.string.message_server_error)
                mainMessageDescr = ""
                imageR = R.drawable.ic_no_data
            }
            else ->
            {
                mainMessage = resources.getString(R.string.message_app_error)
                mainMessageDescr = resources.getString(R.string.message_app_error_descr)
                imageR = R.drawable.ic_delete_txt
            }
        }

        showMessage(mainMessage, mainMessageDescr, imageR)
    }
    private fun getITunesData(){
        hideMessage()
        gettingData()
    }
    private fun gettingData(){
        //getTestData()

        val search = etSearch.text.toString()
        itunesService.search(search).enqueue(object : Callback<ITunesResponse> {

            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>)
            {
                when (response.code())
                {
                    200 ->
                    {
                        if (response.body()?.results?.isNotEmpty() == true)
                        {
                            tracks = response.body()?.results!!
                            currentState = GOOD_RESULT
                            showItunesData()
                        }
                        else
                        {
                            currentState = EMPTY_TRACKS_DATA
                            showInfo(EMPTY_TRACKS_DATA)
                        }
                    }
                    else ->
                    {
                        currentState = SERVER_ERROR
                        showInfo(SERVER_ERROR)
                    }
                }
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable)
            {
                showInfo(SERVER_ERROR)
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
    private fun clearRecyclerView(){
        tracks = ArrayList()
        showItunesData()
    }
    private fun hideItunesData(){
        clearRecyclerView()
        recView.visibility = View.INVISIBLE
    }
    private fun showItunesData(){
        recView.visibility = View.VISIBLE
        adapterTr = TracksAdapter(tracks)
        recView.adapter = adapterTr
        recView.layoutManager = LinearLayoutManager(this)
    }

    //test data
    private fun getTestData(){
        tracks = TestData.getTracks()
        currentState = GOOD_RESULT
    }
}