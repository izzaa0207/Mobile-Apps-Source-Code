package com.capstone.diabesafe.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.diabesafe.MainActivity
import com.capstone.diabesafe.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            delay(3000) // Menunggu 3 detik untuk animasi splash

            val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val isOnboardingSeen = sharedPreferences.getBoolean("isOnboardingSeen", false)

            val userToken = sharedPreferences.getString("user_token", null) // Ambil token yang disimpan

            if (auth.currentUser != null || userToken != null) {
                // Jika ada pengguna yang login atau token masih ada
                val lastScreen = sharedPreferences.getString("last_screen", "home")
                navigateToLastScreen(lastScreen)
            } else {
                // Jika belum login
                if (isOnboardingSeen) {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                }
            }
        }

    }

    private fun navigateToLastScreen(lastScreen: String?) {
        if (auth.currentUser == null) {
            // Jika tidak ada pengguna yang login, langsung ke halaman login
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            // Jika ada pengguna yang login
            when (lastScreen) {
                "home" -> {
                    // Menggunakan Intent untuk memastikan stack aktivitas bersih
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish() // Menutup SplashActivity jika sudah navigasi
                }
                else -> {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

}
