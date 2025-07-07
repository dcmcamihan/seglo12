from pathlib import Path

DATA_DIR = Path("data/gestures")

for class_dir in DATA_DIR.iterdir():
    if class_dir.is_dir():
        num_files = len(list(class_dir.glob("*.npy")))
        print(f"{class_dir.name}: {num_files} samples")