package com.capstone.diabesafe.ui.main.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.capstone.diabesafe.R
import com.capstone.diabesafe.databinding.DialogSaveDataBinding
import com.capstone.diabesafe.utils.loadProfile
import com.capstone.diabesafe.utils.saveProfile
import java.io.File

class SaveDataDialogFragment : DialogFragment() {

    private var _binding: DialogSaveDataBinding? = null
    private val binding get() = _binding!!

    private var selectedPhotoUri: Uri? = null
    private var cameraImageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogSaveDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Autofill existing data
        loadProfile { profileData ->
            if (profileData != null) {
                binding.inputFullName.setText(profileData["fullName"] as? String)
                binding.inputNickname.setText(profileData["nickname"] as? String)
                binding.inputBirthDate.setText(profileData["birthDate"] as? String)
                binding.inputGender.setText(profileData["gender"] as? String)
                val photoUrl = profileData["photoUrl"] as? String
                if (!photoUrl.isNullOrEmpty()) {
                    selectedPhotoUri = Uri.parse(photoUrl)
                    binding.inputPhotoUrl.setText(photoUrl)
                }
            }
        }

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.inputPhotoUrl.setOnClickListener { showImagePickerOptions() }

        binding.btnSave.setOnClickListener {
            val fullName = binding.inputFullName.text.toString()
            val nickname = binding.inputNickname.text.toString()
            val birthDate = binding.inputBirthDate.text.toString()
            val gender = binding.inputGender.text.toString()
            val photoUrl = selectedPhotoUri?.toString() ?: ""

            if (fullName.isNotEmpty() && nickname.isNotEmpty() && birthDate.isNotEmpty() && gender.isNotEmpty()) {
                if (isValidUri(selectedPhotoUri)) {
                    saveProfile(fullName, nickname, birthDate, gender, photoUrl)
                    Toast.makeText(requireContext(),
                        getString(R.string.data_profil_berhasil_disimpan), Toast.LENGTH_SHORT).show()
                    setFragmentResult("saveDataResult", Bundle())
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Foto tidak valid.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.harap_isi_semua_kolom), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.pilih_foto))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "Izin kamera diperlukan untuk mengambil foto.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(requireContext(), "Izin kamera tidak diberikan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraImageUri = createImageUri()
        if (cameraImageUri != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
            takePhotoLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(requireContext(), "Gagal mengakses kamera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            binding.inputPhotoUrl.setText(uri.toString())
        }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            compressImage(cameraImageUri) // Kompres gambar setelah diambil
            selectedPhotoUri = cameraImageUri
            binding.inputPhotoUrl.setText(cameraImageUri.toString())
        } else {
            Toast.makeText(requireContext(), "Gagal mengambil foto.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageUri(): Uri? {
        val timestamp = System.currentTimeMillis()
        val imageFile = File(requireContext().cacheDir, "temp_image_$timestamp.jpg")
        return FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", imageFile)
    }

    private fun isValidUri(uri: Uri?): Boolean {
        return try {
            uri != null && requireContext().contentResolver.openInputStream(uri) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun compressImage(imageUri: Uri?) {
        imageUri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            val compressedFile = File(requireContext().cacheDir, "compressed_${System.currentTimeMillis()}.jpg")

            val fos = requireContext().contentResolver.openOutputStream(Uri.fromFile(compressedFile))
            fos?.let { it1 -> bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it1) } // Compress image with 50% quality
            fos?.close()

            selectedPhotoUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", compressedFile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
