import numpy as np

def extract_hand_landmarks(results):
    """
    Extracts up to 2-hand landmarks (21 each) into a 126-dim flat vector (x, y, z for each landmark).
    Pads with 0s if fewer than 2 hands are detected.
    """
    output = []
    if not results.multi_hand_landmarks:
        return np.zeros(126, dtype=np.float32)

    # Only take up to 2 hands
    hands = results.multi_hand_landmarks[:2]
    for h in hands:
        for lm in h.landmark:
            output.extend([lm.x, lm.y, lm.z])
    # Pad if less than 2 hands
    while len(output) < 126:
        output.extend([0.0] * (126 - len(output)))
    return np.array(output, dtype=np.float32)