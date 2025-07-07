package com.example.seglo.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seglo.helpers.HandLandmarkerHelper
import com.example.seglo.helpers.LabelManager
import com.example.seglo.helpers.ScalerHelper
import com.example.seglo.viewmodels.CameraViewModel
import com.example.seglo.ui.components.CameraControlButtons
import com.example.seglo.ui.components.CameraPreviewCard
import com.example.seglo.ui.components.DetectionResultsCard
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Load labels once
    LaunchedEffect(Unit) {
        LabelManager.loadLabels(context)
        ScalerHelper.loadScaler(context)
    }

    var isCameraActive by remember { mutableStateOf(true) }
    var isProcessing by remember { mutableStateOf(false) }
    val detectionBuffer = remember { mutableStateListOf<String>() }

    var handLandmarkerHelper by remember { mutableStateOf<HandLandmarkerHelper?>(null) }
    var resultBundle by remember { mutableStateOf<HandLandmarkerHelper.ResultBundle?>(null) }
    var inferenceTime by remember { mutableStateOf(0L) }
    var overlayResult by remember { mutableStateOf<HandLandmarkerResult?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    var lastPrediction by remember { mutableStateOf<String?>(null) }
    var stableCount by remember { mutableStateOf(0) }
    var detectedSign by remember { mutableStateOf<String?>(null) }
    var confidence by remember { mutableStateOf(0f) }
    val STABLE_THRESHOLD = 3

    // Initialize HandLandmarkerHelper
    LaunchedEffect(viewModel.currentDelegate, viewModel.currentMaxHands) {
        handLandmarkerHelper = HandLandmarkerHelper(
            context = context,
            runningMode = RunningMode.LIVE_STREAM,
            minHandDetectionConfidence = viewModel.currentMinHandDetectionConfidence,
            minHandTrackingConfidence = viewModel.currentMinHandTrackingConfidence,
            minHandPresenceConfidence = viewModel.currentMinHandPresenceConfidence,
            maxNumHands = viewModel.currentMaxHands,
            currentDelegate = viewModel.currentDelegate,
            handLandmarkerHelperListener = object : HandLandmarkerHelper.LandmarkerListener {
                override fun onError(error: String, errorCode: Int) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(bundle: HandLandmarkerHelper.ResultBundle) {
                    resultBundle = bundle
                    inferenceTime = bundle.inferenceTime
                    overlayResult = bundle.results.firstOrNull()

                    // Prepare input for static gesture model: [1, 126]
                    val landmarksList = bundle.results.firstOrNull()?.landmarks()
                    if (landmarksList != null && landmarksList.isNotEmpty()) {
                        // Flatten all detected hands (up to 2), pad with zeros if less than 2 hands
                        val flatInput = FloatArray(126) { 0f }
                        for (handIdx in 0 until minOf(2, landmarksList.size)) {
                            val hand = landmarksList[handIdx]
                            for (i in 0 until 21) {
                                val baseIdx = handIdx * 63 + i * 3
                                flatInput[baseIdx] = hand[i].x()
                                flatInput[baseIdx + 1] = hand[i].y()
                                flatInput[baseIdx + 2] = hand[i].z()
                            }
                        }

                        // --- Normalize input before inference ---
                        val normalizedInput = ScalerHelper.normalize(flatInput)

                        // Load the model
                        val file = loadModelFile(context, "final_static_model.tflite")
                        val tfliteInterpreter = Interpreter(file)

                        // Prepare output [1, num_classes]
                        val numClasses = LabelManager.getLabelsCount()
                        val output = Array(1) { FloatArray(numClasses) }

                        // Run inference with normalized input
                        tfliteInterpreter.run(arrayOf(normalizedInput), output)
                        tfliteInterpreter.close()

                        // Get predicted label index and label
                        val maxIdx = output[0].indices.maxByOrNull { output[0][it] } ?: -1
                        val conf = output[0].getOrNull(maxIdx) ?: 0f

                        if (conf > 0.7f && maxIdx >= 0) {
                            val label = LabelManager.getLabel(maxIdx)
                            if (label == lastPrediction) {
                                stableCount++
                                if (stableCount >= STABLE_THRESHOLD) {
                                    detectedSign = label
                                    confidence = conf
                                }
                            } else {
                                lastPrediction = label
                                stableCount = 1
                            }
                        }
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        CameraPreviewCard(
            isCameraActive = isCameraActive,
            isProcessing = isProcessing,
            onCameraToggle = {
                isCameraActive = !isCameraActive
                detectedSign = ""
                confidence = 0f
                isProcessing = false
                detectionBuffer.clear()
            },
            cameraContent = {
                if (isCameraActive) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = androidx.camera.view.PreviewView(ctx)
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                cameraProviderFuture.addListener({
                                    cameraProvider = cameraProviderFuture.get()
                                    val cameraSelector = CameraSelector.Builder()
                                        .requireLensFacing(cameraFacing)
                                        .build()
                                    val preview = Preview.Builder()
                                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                                        .build()
                                    val imageAnalyzer = ImageAnalysis.Builder()
                                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                                        .build()
                                        .also {
                                            it.setAnalyzer(executor) { image ->
                                                handLandmarkerHelper?.detectLiveStream(
                                                    imageProxy = image,
                                                    isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
                                                )
                                            }
                                        }
                                    cameraProvider?.unbindAll()
                                    cameraProvider?.bindToLifecycle(
                                        lifecycleOwner, cameraSelector, preview, imageAnalyzer
                                    )
                                    preview.setSurfaceProvider(previewView.surfaceProvider)
                                }, ContextCompat.getMainExecutor(ctx))
                                previewView
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        overlayResult?.let { result ->
                            HandLandmarkOverlay(
                                result = result,
                                imageWidth = resultBundle?.inputImageWidth ?: 1,
                                imageHeight = resultBundle?.inputImageHeight ?: 1,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        )

        CameraControlButtons(
            isCameraActive = isCameraActive,
            isProcessing = isProcessing,
            onCameraToggle = {
                isCameraActive = !isCameraActive
                detectedSign = ""
                confidence = 0f
                isProcessing = false
                detectionBuffer.clear()
            }
        )

        DetectionResultsCard(
            detectedSign = detectedSign.orEmpty(),
            confidence = confidence,
            isProcessing = isProcessing,
            isCameraActive = isCameraActive,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun loadModelFile(context: Context, modelFileName: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelFileName)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}