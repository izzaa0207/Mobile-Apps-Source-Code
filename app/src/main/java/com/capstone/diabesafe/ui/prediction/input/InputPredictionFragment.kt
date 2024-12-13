package com.capstone.diabesafe.ui.prediction.input

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.diabesafe.R
import com.capstone.diabesafe.utils.Predictor
import com.capstone.diabesafe.utils.saveDiagnosis
import com.capstone.diabesafe.databinding.FragmentInputPredictionBinding

class InputPredictionFragment : Fragment() {

    private var _binding: FragmentInputPredictionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tambahkan TextWatcher untuk otomatis menghitung BMI
        binding.inputHeight.addTextChangedListener(bmiTextWatcher)
        binding.inputWeight.addTextChangedListener(bmiTextWatcher)

        // Tombol Prediksi
        binding.btnPredict.setOnClickListener {
            // Hitung ulang BMI sebelum membaca nilainya
            calculateBMI()

            val name = binding.inputName.text.toString()
            val age = binding.inputAge.text.toString().toIntOrNull()
            val height = binding.inputHeight.text.toString().toIntOrNull()
            val weight = binding.inputWeight.text.toString().toIntOrNull()
            val bmiText = binding.inputBmi.text.toString()
            val bmi = bmiText.replace(',', '.').toFloatOrNull()
            val bloodSugar = binding.inputBloodSugar.text.toString().toFloatOrNull()
            val bloodPressure = binding.inputBloodPressure.text.toString().toFloatOrNull() // Sistolik saja

            // Debugging untuk melihat nilai input
            println("Debug: Name: $name, Age: $age, Height: $height, Weight: $weight, BMI: $bmi, BloodPressure: $bloodPressure")
            println("Scaled Inputs: BloodSugar=$bloodSugar, BloodPressure=$bloodPressure, BMI=$bmi, Age=$age")

            if (name.isNotEmpty() && age != null && height != null && weight != null &&
                bmi != null && bloodSugar != null && bloodPressure != null) {
                val predictor = Predictor(requireContext())
                val result = predictor.predict(bloodSugar, bloodPressure, bmi, age.toFloat())

                val diagnosis = if (result > 0.5) "Positive" else "Negative"

                // Simpan ke Firebase
                saveDiagnosis(
                    name, age, height, weight, bmi.toDouble(),
                    bloodSugar.toInt(), bloodPressure.toInt().toString(), diagnosis
                )

                // Navigasi ke fragment hasil
                val navController = findNavController()
                if (diagnosis == "Positive") {
                    navController.navigate(
                        R.id.action_inputFragment_to_resultPositiveFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.inputPredictionFragment, true)
                            .build()
                    )
                } else {
                    navController.navigate(
                        R.id.action_inputFragment_to_resultNegativeFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.inputPredictionFragment, true)
                            .build()
                    )
                }
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.harap_isi_semua_kolom_dengan_benar), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // TextWatcher untuk menghitung BMI secara otomatis
    private val bmiTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calculateBMI()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun calculateBMI() {
        val height = binding.inputHeight.text.toString().toIntOrNull()
        val weight = binding.inputWeight.text.toString().toIntOrNull()

        if (height != null && weight != null && height > 0) {
            val heightInMeters = height / 100.0
            val bmi = weight / (heightInMeters * heightInMeters)
            val formattedBmi = String.format("%.2f", bmi)
            binding.inputBmi.setText(formattedBmi)
            println(getString(R.string.debug_bmi_calculated_as, formattedBmi))
        } else {
            binding.inputBmi.setText("")
            println(getString(R.string.debug_bmi_calculation_failed_invalid_input))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
