package com.practicum.android.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private lateinit var etSearch: EditText
    private lateinit var ivClearText: ImageView

    //--------------------------------------------------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        var searchText = ""
        if (savedInstanceState != null)
            searchText = savedInstanceState.getString(SEARCH_TEXT, "").toString()

        initUI(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, etSearch.text.toString())
    }

    //--------------------------------------------------//
    private fun initUI(searchTxt: String) {
        val tvBack = findViewById<TextView>(R.id.tvBack)
        etSearch = findViewById(R.id.etSearch)
        ivClearText = findViewById(R.id.ivClearText)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty())
                    ivClearText.visibility = View.INVISIBLE
                else
                    ivClearText.visibility = View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etSearch.addTextChangedListener(simpleTextWatcher)
        etSearch.setText(searchTxt)
        tvBack.setOnClickListener { finish() }
        ivClearText.setOnClickListener { clearData() }
    }

    //--------------------------------------------------//
    //region -- Buttons event --
    private fun clearData() {
        etSearch.setText("")
        ivClearText.visibility = View.INVISIBLE

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val currentView = this.currentFocus //this.getCurrentFocus()
        inputMethodManager?.hideSoftInputFromWindow(currentView?.windowToken, 0)
    }
    //endregion
    //--------------------------------------------------//

}