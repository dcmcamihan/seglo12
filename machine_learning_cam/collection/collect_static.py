import cv2
import os
import time
import numpy as np
import mediapipe as mp
import json
from pathlib import Path

# Paths
DATA_DIR = Path("data/gestures")
LABEL_MAP_FILE = Path("labels/label_map.json")
SAMPLES_PER_CLASS = 1000

# MediaPipe setup
mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=False, max_num_hands=2, min_detection_confidence=0.7)
mp_drawing = mp.solutions.drawing_utils

# Create necessary folders
DATA_DIR.mkdir(parents=True, exist_ok=True)
LABEL_MAP_FILE.parent.mkdir(parents=True, exist_ok=True)

# Load or initialize label map
def load_label_map():
    if LABEL_MAP_FILE.exists():
        with open(LABEL_MAP_FILE, "r") as f:
            return json.load(f)
    return {}

def save_label_map(label_map):
    with open(LABEL_MAP_FILE, "w") as f:
        json.dump(label_map, f, indent=4)

# Convert hand landmarks to a flat vector (126 floats: 2 hands * 21 landmarks * x/y/z)
def get_landmark_vector(results):
    output = []
    if results.multi_hand_landmarks:
        for h in results.multi_hand_landmarks:
            for lm in h.landmark:
                output.extend([lm.x, lm.y, lm.z])
    # Pad if only one hand detected
    if len(results.multi_hand_landmarks or []) == 1:
        output.extend([0.0] * (21 * 3))
    return np.array(output, dtype=np.float32)

def main():
    label_map = load_label_map()

    class_label = input("Enter gesture name (e.g., Hello): ").strip().lower()
    hand_type = input("Hand type (left / right / both): ").strip().lower()

    # Update label map if new
    if class_label not in label_map:
        label_map[class_label] = len(label_map)
        save_label_map(label_map)

    label_index = label_map[class_label]
    save_dir = DATA_DIR / f"{class_label}_{hand_type}"
    save_dir.mkdir(parents=True, exist_ok=True)

    # Count existing samples
    existing_files = list(save_dir.glob("*.npy"))
    current_sample = len(existing_files)

    print("\nINSTRUCTIONS:")
    print("Press 'r' to begin recording (with a 3-second countdown).")
    print("Press 'q' anytime during recording to quit.")
    print(f"Existing samples: {current_sample}/{SAMPLES_PER_CLASS}\n")

    cap = cv2.VideoCapture(0)

    while True:
        ret, frame = cap.read()
        if not ret:
            continue

        frame = cv2.flip(frame, 1)
        img_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(img_rgb)

        # Draw hands if detected
        if results.multi_hand_landmarks:
            for h in results.multi_hand_landmarks:
                mp_drawing.draw_landmarks(frame, h, mp_hands.HAND_CONNECTIONS)

        cv2.putText(frame, f"Label: {class_label} ({current_sample}/{SAMPLES_PER_CLASS})", 
                    (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 255), 2)
        cv2.imshow("Collecting", frame)

        key = cv2.waitKey(1)
        if key == ord("q"):
            print("Exiting...")
            break
        elif key == ord("r"):
            print("Recording will start in 3 seconds...")
            for i in range(3, 0, -1):
                print(i)
                time.sleep(1)
            print("Recording started.")
            break

    # Continuous sample collection
    while current_sample < SAMPLES_PER_CLASS:
        ret, frame = cap.read()
        if not ret:
            continue

        frame = cv2.flip(frame, 1)
        img_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(img_rgb)

        if results.multi_hand_landmarks:
            vec = get_landmark_vector(results)
            if vec.shape[0] == 126:
                np.save(save_dir / f"{class_label}_{current_sample:04d}.npy", vec)
                print(f"[✓] Saved sample {current_sample+1}")
                current_sample += 1

        # Show updated frame
        for h in results.multi_hand_landmarks or []:
            mp_drawing.draw_landmarks(frame, h, mp_hands.HAND_CONNECTIONS)
        cv2.putText(frame, f"Label: {class_label} ({current_sample}/{SAMPLES_PER_CLASS})", 
                    (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow("Collecting", frame)

        key = cv2.waitKey(1)
        if key == ord("q"):
            print("Recording interrupted by user.")
            break

        # Halfway prompt
        if current_sample == SAMPLES_PER_CLASS // 2:
            print(f"\n{current_sample} samples collected. Do you want to continue? (y/n): ", end="")
            if input().strip().lower() != "y":
                print("Recording paused by user.")
                break

    cap.release()
    cv2.destroyAllWindows()
    print(f"Collection finished. Total samples: {current_sample}")

if __name__ == "__main__":
    main()