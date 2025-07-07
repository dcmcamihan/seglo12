import numpy as np
import joblib
import json

# Load your scaler if not already in memory
scaler = joblib.load("models/scaler.save")

# Save mean and std as JSON
with open("models/scaler_stats.json", "w") as f:
    json.dump({
        "mean": scaler.mean_.tolist(),
        "std": scaler.scale_.tolist()
    }, f)