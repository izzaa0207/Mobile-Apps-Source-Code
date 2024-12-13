package com.capstone.diabesafe.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

fun saveDiagnosis(
    name: String,
    age: Int,
    height: Int,
    weight: Int,
    bmi: Double,
    bloodSugar: Int,
    bloodPressure: String,
    result: String
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    val uid = auth.currentUser?.uid
    if (uid != null) {
        // Referensi ke database
        val historyRef = database.getReference("users").child(uid).child("history")

        // Data yang akan disimpan
        val timestamp = System.currentTimeMillis().toString()
        val diagnosisData = mapOf(
            "name" to name,
            "age" to age,
            "height" to height,
            "weight" to weight,
            "bmi" to bmi,
            "bloodSugar" to bloodSugar,
            "bloodPressure" to bloodPressure,
            "result" to result
        )

        // Simpan data ke database
        historyRef.child(timestamp).setValue(diagnosisData)
            .addOnSuccessListener {
                println("Data diagnosis berhasil disimpan.")
            }
            .addOnFailureListener { e ->
                println("Gagal menyimpan data: ${e.message}")
            }
    } else {
        println("Pengguna belum login.")
    }
}

fun loadHistory(onDataLoaded: (List<Map<String, Any>>) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    val uid = auth.currentUser?.uid
    if (uid != null) {
        val historyRef = database.getReference("users").child(uid).child("history")

        historyRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val historyList = mutableListOf<Map<String, Any>>()
                    for (child in snapshot.children) {
                        val diagnosis = child.value as Map<String, Any>
                        historyList.add(diagnosis)
                    }
                    onDataLoaded(historyList)
                } else {
                    println("Data history tidak ditemukan.")
                    onDataLoaded(emptyList())
                }
            }
            .addOnFailureListener { e ->
                println("Gagal memuat data: ${e.message}")
            }
    } else {
        println("Pengguna belum login.")
        onDataLoaded(emptyList())
    }
}

// Bagian Profile
fun saveProfile(
    fullName: String,
    nickname: String,
    birthDate: String,
    gender: String,
    photoUrl: String
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    val uid = auth.currentUser?.uid
    if (uid != null) {
        // Referensi ke database
        val profileRef = database.getReference("users").child(uid).child("profile")

        // Data profil yang akan disimpan
        val profileData = mapOf(
            "fullName" to fullName,
            "nickname" to nickname,
            "birthDate" to birthDate,
            "gender" to gender,
            "photoUrl" to photoUrl
        )

        // Simpan data ke database
        profileRef.setValue(profileData)
            .addOnSuccessListener {
                println("Data profil berhasil disimpan.")
            }
            .addOnFailureListener { e ->
                println("Gagal menyimpan data: ${e.message}")
            }
    } else {
        println("Pengguna belum login.")
    }
}

fun loadProfile(onDataLoaded: (Map<String, Any>?) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    val uid = auth.currentUser?.uid
    if (uid != null) {
        val profileRef = database.getReference("users").child(uid).child("profile")

        profileRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val profileData = snapshot.value as Map<String, Any>
                    onDataLoaded(profileData)
                } else {
                    println("Data profil tidak ditemukan.")
                    onDataLoaded(null)
                }
            }
            .addOnFailureListener { e ->
                println("Gagal memuat data: ${e.message}")
                onDataLoaded(null)
            }
    } else {
        println("Pengguna belum login.")
        onDataLoaded(null)
    }
}



