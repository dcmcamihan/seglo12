from pathlib import Path
import shutil

DATA_DIR = Path("data/gestures")

def delete_class_folder(class_name, hand_type):
    folder = DATA_DIR / f"{class_name}_{hand_type}"
    if folder.exists() and folder.is_dir():
        shutil.rmtree(folder)
        print(f"Deleted folder: {folder}")
    else:
        print(f"Folder not found: {folder}")

if __name__ == "__main__":
    class_name = input("Enter the gesture name to delete: ").strip().lower()
    hand_type = input("Enter hand type (left/right/both): ").strip().lower()
    delete_class_folder(class_name, hand_type)