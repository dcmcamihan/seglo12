package com.example.seglo.viewmodels

import androidx.lifecycle.ViewModel
import com.example.seglo.helpers.HandLandmarkerHelper

/**
 * ViewModel for storing hand landmarker helper settings.
 * Aligned for static gesture recognition with up to 2 hands (left, right, or both).
 */
class CameraViewModel : ViewModel() {

    private var _delegate: Int = HandLandmarkerHelper.DELEGATE_CPU
    private var _minHandDetectionConfidence: Float = HandLandmarkerHelper.DEFAULT_HAND_DETECTION_CONFIDENCE
    private var _minHandTrackingConfidence: Float = HandLandmarkerHelper.DEFAULT_HAND_TRACKING_CONFIDENCE
    private var _minHandPresenceConfidence: Float = HandLandmarkerHelper.DEFAULT_HAND_PRESENCE_CONFIDENCE
    private var _maxHands: Int = HandLandmarkerHelper.DEFAULT_NUM_HANDS // Should be 2 for left/right/both

    val currentDelegate: Int get() = _delegate
    val currentMinHandDetectionConfidence: Float get() = _minHandDetectionConfidence
    val currentMinHandTrackingConfidence: Float get() = _minHandTrackingConfidence
    val currentMinHandPresenceConfidence: Float get() = _minHandPresenceConfidence
    val currentMaxHands: Int get() = _maxHands

    fun setDelegate(delegate: Int) {
        _delegate = delegate
    }

    fun setMinHandDetectionConfidence(confidence: Float) {
        _minHandDetectionConfidence = confidence
    }

    fun setMinHandTrackingConfidence(confidence: Float) {
        _minHandTrackingConfidence = confidence
    }

    fun setMinHandPresenceConfidence(confidence: Float) {
        _minHandPresenceConfidence = confidence
    }

    fun setMaxHands(maxHands: Int) {
        _maxHands = maxHands
    }
}