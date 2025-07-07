package com.example.seglo.helpers

import android.content.Context
import org.json.JSONObject

object LabelManager {
    private var labels: List<String>? = null

    fun loadLabels(context: Context, assetFileName: String = "label_map.json") {
        if (labels != null) return // Already loaded
        val jsonString = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        // Sort by value (index) to ensure correct order
        labels = jsonObject.keys().asSequence()
            .map { key -> key to jsonObject.getInt(key) }
            .sortedBy { it.second }
            .map { it.first }
            .toList()
    }

    fun getLabel(index: Int): String {
        return labels?.getOrNull(index) ?: "Unknown"
    }

    fun getLabelsCount(): Int {
        return labels?.size ?: 0
    }
}