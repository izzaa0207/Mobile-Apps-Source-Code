package com.capstone.diabesafe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.diabesafe.utils.LanguageUtil

class PredictionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)
        // Terapkan bahasa yang tersimpan
        val languageCode = LanguageUtil.getSavedLanguage(this)
        LanguageUtil.setLocale(this, languageCode)
        supportActionBar?.hide()
    }
}