import tensorflow as tf
import os

MODEL_PATH = "models/final_static_model.h5"
TFLITE_PATH = "models/final_static_model.tflite"

def convert_model():
    if not os.path.exists(MODEL_PATH):
        raise FileNotFoundError(f"Model not found at: {MODEL_PATH}")

    model = tf.keras.models.load_model(MODEL_PATH)

    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]  # Reduce size, improve latency
    tflite_model = converter.convert()

    with open(TFLITE_PATH, "wb") as f:
        f.write(tflite_model)

    print(f"Model successfully converted to TFLite:\n→ {TFLITE_PATH}")

if __name__ == "__main__":
    convert_model()