import cv2
import numpy as np
import mediapipe as mp
import tensorflow as tf
import joblib
import json
from utils.vectorizer import extract_hand_landmarks

# Load model and scaler
model = tf.keras.models.load_model("models/final_static_model.h5")
scaler = joblib.load("models/scaler.save")
with open("labels/label_map.json", "r") as f:
    label_map = json.load(f)
inv_label_map = {v: k for k, v in label_map.items()}

# Setup MediaPipe
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=False, max_num_hands=2, min_detection_confidence=0.7)
mp_drawing = mp.solutions.drawing_utils

def main():
    cap = cv2.VideoCapture(0)

    print("\n[INFO] Press 'q' to quit.")
    while True:
        ret, frame = cap.read()
        if not ret:
            continue

        frame = cv2.flip(frame, 1)
        img_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(img_rgb)

        # Only predict if at least one hand is detected
        if not results.multi_hand_landmarks:
            cv2.putText(frame, "No hands detected", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 
                        1, (0, 0, 255), 2, cv2.LINE_AA)
        else:
            vector = extract_hand_landmarks(results)
            if len(vector) != 126:
                cv2.putText(frame, "Invalid vector size", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 
                            1, (0, 0, 255), 2, cv2.LINE_AA)
            else:
                vector_scaled = scaler.transform([vector])
                prediction = np.argmax(model.predict(vector_scaled), axis=1)[0]
                label = inv_label_map[prediction]

                for hand_landmarks in results.multi_hand_landmarks:
                    mp_drawing.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)

                cv2.putText(frame, f"Prediction: {label}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 
                            1, (0, 255, 0), 2, cv2.LINE_AA)

        cv2.imshow("Real-time Gesture Recognition", frame)
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()