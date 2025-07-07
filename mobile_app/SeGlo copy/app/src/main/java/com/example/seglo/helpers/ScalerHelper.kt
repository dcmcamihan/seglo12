package com.example.seglo.helpers

import android.content.Context
import org.json.JSONObject

object ScalerHelper {
    private var mean: FloatArray? = null
    private var std: FloatArray? = null

    fun loadScaler(context: Context, assetFileName: String = "scaler_stats.json") {
        if (mean != null && std != null) return
        val jsonString = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        mean = jsonObject.getJSONArray("mean").let { arr -> FloatArray(arr.length()) { arr.getDouble(it).toFloat() } }
        std = jsonObject.getJSONArray("std").let { arr -> FloatArray(arr.length()) { arr.getDouble(it).toFloat() } }
    }

    fun normalize(input: FloatArray): FloatArray {
        val m = mean ?: error("Scaler mean not loaded")
        val s = std ?: error("Scaler std not loaded")
        return FloatArray(input.size) { i ->
            if (s[i] != 0f) (input[i] - m[i]) / s[i] else 0f
        }
    }
}