package com.capstone.diabesafe.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.capstone.diabesafe.AuthActivity
import com.capstone.diabesafe.R
import com.capstone.diabesafe.databinding.FragmentProfileBinding
import com.capstone.diabesafe.utils.LanguageUtil
import com.capstone.diabesafe.utils.loadProfile
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding // No forced unwrapping

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root // Safe access to binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Load user profile
        loadUserProfile()

        loadUserEmail()

        setFragmentResultListener("saveDataResult") { _, _ ->
            // Refresh user profile when dialog finishes saving data
            loadUserProfile()
        }

        // Edit profile button
        binding?.btnEditProfile?.setOnClickListener {
            val dialog = SaveDataDialogFragment()
            dialog.show(parentFragmentManager, "SaveDataDialogFragment")
        }

        // Logout button
        binding?.btnLogout?.setOnClickListener {
            showLogoutDialog()
        }

        // Language change button
        binding?.btnEditBahasa?.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun loadUserEmail() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding?.emailUser?.text = currentUser.email ?: getString(R.string.email_unknown)
        }
    }

    private fun loadUserProfile() {
        loadProfile { profileData ->
            if (profileData != null) {
                binding?.profileName?.text = profileData["fullName"] as String? ?: getString(R.string.name_unknown)
                binding?.profileNickname?.text = profileData["nickname"] as String? ?: getString(R.string.nickname_unknown)
                binding?.profileBirthDate?.text = profileData["birthDate"] as String? ?: getString(R.string.birt_date_unknown)
                binding?.profileGender?.text = profileData["gender"] as String? ?: getString(R.string.gender_unknown)

                val photoUrl = profileData["photoUrl"] as String?
                if (!photoUrl.isNullOrEmpty()) {
                    if (isAdded && context != null) {
                        Glide.with(this)
                            .load(photoUrl)
                            .circleCrop()
                            .into(binding?.profileImage!!)
                    }
                } else {
                    if (isAdded && context != null) {
                        Glide.with(this)
                            .load(R.drawable.ic_person)  // Gunakan gambar default
                            .circleCrop()
                            .into(binding?.profileImage!!)
                    }
                }
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.konfirmasi_logout))
        builder.setMessage(getString(R.string.konfirmasi_logout_dialog))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            logout()
        }

        builder.setNegativeButton(getString(R.string.cancle)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun logout() {
        auth.signOut()
        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("last_screen", "login").apply()

        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Bahasa Indonesia", "English")
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.pilih_bahasa))

        builder.setItems(languages) { dialog, which ->
            val selectedLanguage = when (which) {
                0 -> "id" // Bahasa Indonesia
                1 -> "en" // English
                else -> "en"
            }
            changeLanguage(selectedLanguage)
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun changeLanguage(languageCode: String) {
        LanguageUtil.saveLanguage(requireContext(), languageCode)
        LanguageUtil.setLocale(requireContext(), languageCode)

        Toast.makeText(requireContext(), getString(R.string.language_changed), Toast.LENGTH_SHORT).show()
        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set binding to null after view is destroyed
    }
}
