import os
import numpy as np
from pathlib import Path
import json
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

DATA_DIR = Path("data/gestures")
LABEL_MAP_FILE = Path("labels/label_map.json")

def load_label_map():
    with open(LABEL_MAP_FILE, "r") as f:
        return json.load(f)

def load_data(test_split=0.2):
    X, y = [], []
    label_map = load_label_map()

    for label_name, label_index in label_map.items():
        # Find all subfolders that start with the label name (e.g., a_left, a_right)
        for class_dir in DATA_DIR.glob(f"{label_name}_*"):
            if not class_dir.is_dir():
                continue
            for file in class_dir.glob("*.npy"):
                X.append(np.load(file))
                y.append(label_index)

    X, y = np.array(X), np.array(y)

    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    X_train, X_test, y_train, y_test = train_test_split(
        X_scaled, y, test_size=test_split, stratify=y, random_state=42
    )
    return X_train, X_test, y_train, y_test, scaler, label_map