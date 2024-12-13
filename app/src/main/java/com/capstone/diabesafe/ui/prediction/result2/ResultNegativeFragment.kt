package com.capstone.diabesafe.ui.prediction.result2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.diabesafe.MainActivity
import com.capstone.diabesafe.databinding.FragmentResultNegativeBinding

class ResultNegativeFragment : Fragment() {

    private var _binding: FragmentResultNegativeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultNegativeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol kembali ke halaman utama
        binding.btnBackToHome.setOnClickListener {
            // Navigasi ke MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish() // Mengakhiri aktivitas saat ini
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
