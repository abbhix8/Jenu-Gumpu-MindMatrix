# TensorFlow Lite Model Setup Guide

## 📦 Model Requirements

The app can work **without** a TensorFlow Lite model by using color-based fallback analysis. However, for accurate AI-based grading, you can add a custom model.

---

## 🎯 Model Specifications

### Input
- **Format**: RGB image
- **Size**: 224 x 224 pixels
- **Type**: Float32
- **Shape**: [1, 224, 224, 3]
- **Normalization**: Pixel values 0.0 to 1.0

### Output
- **Format**: Probabilities for 3 classes
- **Type**: Float32
- **Shape**: [1, 3]
- **Classes**:
  - Index 0: Light honey
  - Index 1: Medium honey
  - Index 2: Dark honey

---

## 📁 Installation

1. **Create assets folder** (if it doesn't exist):
   ```
   app/src/main/assets/
   ```

2. **Add your model**:
   - Copy your `.tflite` file to: `app/src/main/assets/`
   - Rename it to: `honey_grade_model.tflite`

3. **Rebuild the app**:
   - The app will automatically detect and load the model
   - If model loading fails, it falls back to color analysis

---

## 🧠 Training Your Own Model

If you want to train a custom model, here's a basic TensorFlow approach:

### Step 1: Collect Data
- Take 200+ photos of honey samples
- Organize into folders:
  ```
  dataset/
  ├── light/
  ├── medium/
  └── dark/
  ```

### Step 2: Train Model (Python + TensorFlow)

```python
import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# Load data
train_datagen = ImageDataGenerator(
    rescale=1./255,
    validation_split=0.2,
    rotation_range=20,
    zoom_range=0.2,
    horizontal_flip=True
)

train_generator = train_datagen.flow_from_directory(
    'dataset/',
    target_size=(224, 224),
    batch_size=32,
    class_mode='categorical',
    subset='training'
)

validation_generator = train_datagen.flow_from_directory(
    'dataset/',
    target_size=(224, 224),
    batch_size=32,
    class_mode='categorical',
    subset='validation'
)

# Build model
model = models.Sequential([
    layers.Conv2D(32, (3, 3), activation='relu', input_shape=(224, 224, 3)),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(64, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(128, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Flatten(),
    layers.Dense(128, activation='relu'),
    layers.Dropout(0.5),
    layers.Dense(3, activation='softmax')  # 3 classes
])

# Compile
model.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

# Train
history = model.fit(
    train_generator,
    epochs=20,
    validation_data=validation_generator
)

# Save as TFLite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

with open('honey_grade_model.tflite', 'wb') as f:
    f.write(tflite_model)

print("Model saved as honey_grade_model.tflite")
```

### Step 3: Test Model
```python
import tensorflow as tf
import numpy as np
from PIL import Image

# Load model
interpreter = tf.lite.Interpreter(model_path="honey_grade_model.tflite")
interpreter.allocate_tensors()

# Get input/output details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Load and preprocess image
img = Image.open('test_honey.jpg').resize((224, 224))
img_array = np.array(img, dtype=np.float32) / 255.0
img_array = np.expand_dims(img_array, axis=0)

# Run inference
interpreter.set_tensor(input_details[0]['index'], img_array)
interpreter.invoke()
output_data = interpreter.get_tensor(output_details[0]['index'])

# Get prediction
classes = ['Light', 'Medium', 'Dark']
predicted_class = classes[np.argmax(output_data)]
confidence = np.max(output_data)

print(f"Prediction: {predicted_class} ({confidence:.2%})")
```

---

## 🔄 Using Pre-trained Models

You can also use transfer learning with pre-trained models:

### MobileNetV2 (Recommended for mobile)
```python
from tensorflow.keras.applications import MobileNetV2

base_model = MobileNetV2(
    input_shape=(224, 224, 3),
    include_top=False,
    weights='imagenet'
)

base_model.trainable = False

model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(128, activation='relu'),
    layers.Dropout(0.5),
    layers.Dense(3, activation='softmax')
])

# Train and convert to TFLite
```

---

## 🎯 Model Optimization

### Quantization (for smaller size)
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quantized_model = converter.convert()
```

### Float16 (balance size/accuracy)
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]
tflite_fp16_model = converter.convert()
```

---

## ✅ Verification

After adding the model:

1. **Run the app**
2. **Go to Camera screen**
3. **Capture a honey image**
4. **Check the result**:
   - If model loaded: You'll see accurate grade with high confidence
   - If fallback: You'll see "⚠ Using color analysis (model not loaded)"

---

## 🐛 Troubleshooting

### Model not loading?
- Check file name: must be exactly `honey_grade_model.tflite`
- Check location: `app/src/main/assets/honey_grade_model.tflite`
- Check file size: shouldn't be empty
- Check logcat for errors: `adb logcat | grep TensorFlow`

### Low accuracy?
- Train with more diverse data
- Balance dataset (equal light/medium/dark samples)
- Data augmentation during training
- Try transfer learning with MobileNetV2

### App crashes on inference?
- Ensure input shape is correct (224x224x3)
- Check output shape is (1, 3)
- Verify model is converted properly

---

## 📚 Resources

- [TensorFlow Lite Guide](https://www.tensorflow.org/lite/guide)
- [TFLite Model Maker](https://www.tensorflow.org/lite/guide/model_maker)
- [MobileNet Models](https://github.com/tensorflow/models/tree/master/research/slim/nets/mobilenet)
- [Image Classification Tutorial](https://www.tensorflow.org/lite/examples/image_classification/overview)

---

## 💡 Quick Start (No Model)

**Don't have a model?** No problem!

The app works perfectly fine without a TensorFlow Lite model. It uses:
- Color analysis to estimate honey grade
- Brightness and RGB ratio calculations
- Reasonable accuracy for basic classification

This fallback is sufficient for initial testing and demonstration!

---

**Happy Model Training!** 🤖🍯
