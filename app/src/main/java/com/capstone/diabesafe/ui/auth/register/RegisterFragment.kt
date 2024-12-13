package com.capstone.diabesafe.ui.auth.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.diabesafe.R
import com.capstone.diabesafe.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        // Observasi hasil registrasi
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is RegisterViewModel.Result.Loading -> showLoading(true)
                is RegisterViewModel.Result.Success -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is RegisterViewModel.Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val confirmPassword = binding.inputConfirmPassword.text.toString().trim()

            if (validateInput(email, password, confirmPassword)) {
                viewModel.registerUser(email, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            binding.inputEmail.error = getString(R.string.email_harus_diisi)
            return false
        }
        if (password.isEmpty()) {
            binding.inputPassword.error = getString(R.string.password_harus_diisi)
            return false
        }
        if (confirmPassword.isEmpty()) {
            binding.inputConfirmPassword.error = getString(R.string.konfirmasi_password_harus_diisi)
            return false
        }
        if (password != confirmPassword) {
            binding.inputConfirmPassword.error = getString(R.string.password_tidak_sesuai)
            return false
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.titleRegister.isInvisible = isLoading
        binding.registerCard.isInvisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
