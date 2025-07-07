import numpy as np
import tensorflow as tf

TFLITE_PATH = "models/final_static_model.tflite"

def check_tflite_model():
    interpreter = tf.lite.Interpreter(model_path=TFLITE_PATH)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    print("Input Details:", input_details)
    print("Output Details:", output_details)

    dummy_input = np.random.rand(1, 126).astype(np.float32)
    interpreter.set_tensor(input_details[0]["index"], dummy_input)
    interpreter.invoke()
    output_data = interpreter.get_tensor(output_details[0]["index"])

    print(f"Inference successful. Output shape: {output_data.shape}, Output: {output_data}")

if __name__ == "__main__":
    check_tflite_model()