# 🤟 SeGlo-12: A Multimodal Glove Integrating Flex Sensors and Computer Vision for Sign-to-Speech Translation

**SeGlo** is a hybrid, mobile-based assistive communication tool that translates American Sign Language (ASL) gestures into both text and speech in real-time. It supports two independent input modes—a wearable smart glove and a camera-based recognition system—integrated into a single offline-capable Android application.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Hardware Components](#hardware-components)
- [Mobile App (Kotlin)](#mobile-app-kotlin)
- [Machine Learning (Python)](#machine-learning-python)
- [Usage](#usage)
- [Installation](#installation)
- [Contributors](#contributors)

---

## 🧠 Overview

Millions of people globally rely on sign language for communication, but most non-signers cannot understand it. Existing solutions often focus on either glove-based or camera-based recognition—rarely both—and face limitations like poor lighting conditions, lack of flexibility, or the need for internet connectivity.

**SeGlo bridges this gap** by combining:
- A **sensor glove** for tactile accuracy and phrase mapping.
- A **vision-based classifier** for hands-free recognition.
- A unified **mobile app** that works **entirely offline**.

---

## ✅ Features

- 🔀 **Dual Recognition Modes** – Switch between Glove Mode and Camera Mode
- 📱 **Offline Operation** – No internet required
- 🔊 **Text-to-Speech Support** – Real-time vocal output for recognized signs
- ⚡ **Fast On-Device Inference** – TensorFlow Lite optimized models
- 🧤 **Calibrated Glove Gestures** – Includes static ASL letters and predefined phrases
- 📸 **Camera-Based Hand Tracking** – Detects single or two-handed static gestures

---

## 🧰 Hardware Components

| Component           | Purpose                                  |
|---------------------|------------------------------------------|
| **Flex Sensors (x5)** | Detect finger bends                      |
| **MPU-6050 Gyroscope** | Capture hand orientation and tilt        |
| **Arduino Nano**     | Reads sensor data and applies logic      |
| **HC-05 Bluetooth**  | Sends gesture output to the mobile app   |
| **Android Phone**    | Runs the app and performs classification |

---

## 📲 Mobile App (Kotlin)

- Developed using **Kotlin** and **Jetpack Compose**
- Includes **Bluetooth integration**, **CameraX**, **MediaPipe**, and **TFLite**
- Features a responsive UI with support for light and dark modes
- Real-time recognition and display of ASL gestures
- Audio output via **Android's Text-to-Speech API**
- Switch between Glove and Camera Modes manually

---

## 🧪 Machine Learning (Python)

- Developed using **TensorFlow**, **MediaPipe**, **NumPy**, and **scikit-learn**
- Uses MediaPipe to extract 3D hand landmarks (21 keypoints per hand)
- Features:
  - Data preprocessing and normalization
  - Dense neural network classifier
  - Export to `.tflite` format for mobile integration

---

## ▶️ Usage

1. **Glove Mode**:
   - Wear the SeGlo glove.
   - Connect via Bluetooth in the app.
   - Perform static ASL gestures or mapped phrases.
   - The app displays and vocalizes the result.

2. **Camera Mode**:
   - Point your hand(s) toward the smartphone camera.
   - Perform a static ASL gesture.
   - The app extracts landmarks, classifies the sign, and speaks it aloud.

---

## ⚙️ Installation

### 📱 Android App
1. Clone this repository and open the `mobile_app/` folder in Android Studio.
2. Place the `.tflite` model and `label_map.txt` into the `assets/` directory.
3. Build and run the app on a physical Android device.

### 🧠 Machine Learning (Python)
```bash
cd machine_learning_cam/SeGlo copy
pip install -r requirements.txt
python train_model.py
python convert_to_tflite.py
```

---

## 👥 Contributors

- **Herald Vann Alalim**  
- **Dianna Claire Marie Amihan**  
- **Cyril Angelo Soto**

© 2025 **SeGlo Team**. All rights reserved.

