package com.practicum.android.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        initUI()
    }

    private fun initUI() {
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnMedia = findViewById<Button>(R.id.btnMedia)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        btnSearch.setOnClickListener { openActivity(this, SearchActivity::class.java) }
        btnMedia.setOnClickListener { openActivity(this, MediatekaActivity::class.java) }
        btnSettings.setOnClickListener { openActivity(this, SettingsActivity::class.java) }
    }

    private fun Context.openActivity(activity: Activity, activityClass: Class<*>?) {
        val intent = Intent(activity, activityClass)
        startActivity(intent)
    }
}