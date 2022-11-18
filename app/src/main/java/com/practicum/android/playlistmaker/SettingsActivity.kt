package com.practicum.android.playlistmaker


import android.annotation.SuppressLint
//import android.content.res.Configuration
//import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {

    companion object {
        const val DARK_THEME = "DARK_THEME"
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var swDarkTheme: Switch

    //--------------------------------------------------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        var darkTheme = false
        if (savedInstanceState != null)
            darkTheme = savedInstanceState.getBoolean(DARK_THEME, false)
        //else
        //    darkTheme = isDarkTheme()

        initUI(darkTheme)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DARK_THEME, swDarkTheme.isChecked)
    }

    //--------------------------------------------------//
    private fun initUI(_isDarkTheme: Boolean) {
        val tvBack = findViewById<TextView>(R.id.tvBack)
        swDarkTheme = findViewById(R.id.swDarkTheme)
        val tvShareApp = findViewById<TextView>(R.id.tvShareApp)
        val tvSupport = findViewById<TextView>(R.id.tvSupport)
        val tvTermOfUse = findViewById<TextView>(R.id.tvTermOfUse)

        tvBack.setOnClickListener { finish() }
        swDarkTheme.isChecked = _isDarkTheme
        swDarkTheme.setOnCheckedChangeListener { _, isChecked -> setTheme(isChecked) } // buttonView == _
        tvShareApp.setOnClickListener { shareApplication() }
        tvSupport.setOnClickListener { writeToTheSupport() }
        tvTermOfUse.setOnClickListener { openFullTextTermOfUse() }
    }

    /*
    private fun Context.isDarkTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    */

    //--------------------------------------------------//
    //region -- Buttons event --
    private fun setTheme(isDark:Boolean) {
        if(isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun shareApplication() {
        val message: String = getString(R.string.share_message)
        val title: String = getString(R.string.share_title)

        val shrIntent = Intent(Intent.ACTION_SEND)
        shrIntent.putExtra(Intent.EXTRA_TEXT, message)
        shrIntent.type = "text/plain"
        startActivity(Intent.createChooser(shrIntent, title))
    }

    private fun writeToTheSupport() {
        val message: String = getString(R.string.mail_message)
        val mailAddress: String = getString(R.string.mail_address)
        val subject: String = getString(R.string.mail_header)
        val uriString: String = getString(R.string.mail_uriString)
        val title: String = getString(R.string.mail_title)

        val wrIntent = Intent(Intent.ACTION_SENDTO)
        wrIntent.data = Uri.parse(uriString)
        wrIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        wrIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailAddress))
        wrIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(wrIntent, title))
    }

    private fun openFullTextTermOfUse() {
        val httpAddress: String = getString(R.string.tou_url)

        val urlIntent = Intent(Intent.ACTION_VIEW)
        urlIntent.data = Uri.parse(httpAddress)
        startActivity(urlIntent)
    }
    //endregion
    //--------------------------------------------------//
}