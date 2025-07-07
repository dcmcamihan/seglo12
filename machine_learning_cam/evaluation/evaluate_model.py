import tensorflow as tf
import joblib
import numpy as np
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, f1_score
from models.train_utils import load_data
import json
import matplotlib.pyplot as plt
import seaborn as sns

def main():
    # Load data
    X_train, X_test, y_train, y_test, scaler, label_map = load_data()
    label_names = {v: k for k, v in label_map.items()}

    # Load model
    model = tf.keras.models.load_model("models/final_static_model.h5")
    y_pred = np.argmax(model.predict(X_test), axis=1)

    # Metrics
    print("\nClassification Report:\n", classification_report(y_test, y_pred, target_names=list(label_names.values())))
    print("\nF1 Score (macro):", f1_score(y_test, y_pred, average='macro'))
    print("Accuracy:", accuracy_score(y_test, y_pred))

    # Confusion Matrix
    cm = confusion_matrix(y_test, y_pred)
    plt.figure(figsize=(10, 8))
    sns.heatmap(cm, annot=True, fmt='d', xticklabels=label_names.values(), yticklabels=label_names.values(), cmap='Blues')
    plt.title("Confusion Matrix")
    plt.xlabel("Predicted")
    plt.ylabel("Actual")
    plt.tight_layout()
    plt.savefig("evaluation/confusion_matrix.png")
    plt.show()

if __name__ == "__main__":
    main()