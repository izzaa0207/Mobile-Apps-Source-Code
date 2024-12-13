package com.capstone.diabesafe.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Predictor(context: Context) {

    private val interpreter: Interpreter

    // Tambahkan nilai mean dan std (atau min dan max untuk MinMaxScaler)
    private val mean = floatArrayOf(120.0f, 80.0f, 25.0f, 40.0f)
    private val std = floatArrayOf(30.0f, 10.0f, 5.0f, 15.0f)

    init {
        val assetManager = context.assets
        val model = assetManager.open("model.tflite")
        val modelBytes = model.readBytes()
        model.close()

        val buffer = ByteBuffer.allocateDirect(modelBytes.size)
        buffer.put(modelBytes)
        buffer.flip()

        interpreter = Interpreter(buffer)
    }

    fun predict(bloodSugar: Float, bloodPressure: Float, bmi: Float, age: Float): Float {
        // Buat array input
        val input = floatArrayOf(bloodSugar, bloodPressure, bmi, age)

        // Terapkan standardisasi pada input
        val scaledInput = FloatArray(input.size)
        for (i in input.indices) {
            scaledInput[i] = (input[i] - mean[i]) / std[i]
        }

        // Masukkan data terstandardisasi ke dalam buffer
        val inputBuffer = ByteBuffer.allocateDirect(4 * scaledInput.size).order(ByteOrder.nativeOrder())
        scaledInput.forEach { inputBuffer.putFloat(it) }
        inputBuffer.rewind()

        val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        interpreter.run(inputBuffer, outputBuffer)

        outputBuffer.rewind()
        return outputBuffer.float
    }
}
