import tensorflow as tf
from tensorflow.keras import layers, models, callbacks
from .train_utils import load_data
import os

def build_model(input_dim, num_classes):
    model = models.Sequential([
        layers.Input(shape=(input_dim,)),
        layers.Dense(256, activation='relu'),
        layers.Dropout(0.3),
        layers.Dense(128, activation='relu'),
        layers.Dropout(0.3),
        layers.Dense(64, activation='relu'),
        layers.Dense(num_classes, activation='softmax')
    ])
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    return model

def main():
    X_train, X_test, y_train, y_test, scaler, label_map = load_data()
    model = build_model(X_train.shape[1], len(label_map))

    checkpoint = callbacks.ModelCheckpoint(
        "models/static_model.h5", monitor="val_accuracy", save_best_only=True
    )

    history = model.fit(
        X_train, y_train,
        validation_data=(X_test, y_test),
        epochs=50,
        batch_size=32,
        callbacks=[checkpoint]
    )

    model.save("models/final_static_model.h5")
    print("Training complete. Model saved to models/")

    # Save scaler
    import joblib
    joblib.dump(scaler, "models/scaler.save")

if __name__ == "__main__":
    main()