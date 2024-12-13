package com.capstone.diabesafe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.diabesafe.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: List<Map<String, Any>>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Map<String, Any>) {
            binding.textName.text = data["name"].toString()
            binding.textAge.text = "Age: ${data["age"]}"
            binding.textHeight.text = "Height: ${data["height"]} cm"
            binding.textWeight.text = "Weight: ${data["weight"]} kg"
            binding.textBMI.text = "BMI: ${data["bmi"]}"
            binding.textBloodSugar.text = "Blood Sugar: ${data["bloodSugar"]} mg/dL"
            binding.textBloodPressure.text = "Blood Pressure: ${data["bloodPressure"]}"
            binding.textResult.text = "Result: ${data["result"]}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historyList[position]
        holder.bind(data)
    }

    override fun getItemCount() = historyList.size
}
