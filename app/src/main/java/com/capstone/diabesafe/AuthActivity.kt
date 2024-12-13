package com.capstone.diabesafe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.capstone.diabesafe.utils.LanguageUtil


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Terapkan bahasa yang tersimpan
        val languageCode = LanguageUtil.getSavedLanguage(this)
        LanguageUtil.setLocale(this, languageCode)
        supportActionBar?.hide()
    }
}
