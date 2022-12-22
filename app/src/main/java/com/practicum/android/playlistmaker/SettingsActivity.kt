package com.practicum.android.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial


const val PM_PREFERENCE = "play_maker_preference"

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var shPrefs: SharedPreferences

    //--------------------------------------------------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        initUI()
    }

    //--------------------------------------------------//
    private fun initUI() {
        val tvBack = findViewById<TextView>(R.id.tvBack)

        themeSwitcher = findViewById(R.id.themeSwitcher)
        val tvShareApp = findViewById<TextView>(R.id.tvShareApp)
        val tvSupport = findViewById<TextView>(R.id.tvSupport)
        val tvTermOfUse = findViewById<TextView>(R.id.tvTermOfUse)

        tvShareApp.setOnClickListener { shareApplication() }
        tvSupport.setOnClickListener { writeToTheSupport() }
        tvTermOfUse.setOnClickListener { openFullTextTermOfUse() }
        tvBack.setOnClickListener { finish() }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            saveTheme(checked)
        }
        shPrefs = getSharedPreferences(PM_PREFERENCE, MODE_PRIVATE)
        val darkTheme = shPrefs.getBoolean((applicationContext as App).THEME_KEY, false)
        if (darkTheme)
            themeSwitcher.isChecked = true
    }

    private fun saveTheme(checked: Boolean) {
        shPrefs.edit()
            .putBoolean((applicationContext as App).THEME_KEY, checked)
            .apply()
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