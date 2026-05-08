# ⚡ Quick Start Guide

Get Jenu-Gumpu running in **5 minutes**!

---

## 🚀 Fast Track

### 1. Open in Android Studio
```bash
# Clone or open the project
cd JenuGumpu
```

### 2. Sync Dependencies
- Android Studio will prompt you to sync Gradle
- Click **"Sync Now"**
- Wait for completion (first time may take 2-3 minutes)

### 3. Run the App
- Connect Android device **OR** start emulator
- Click the green **Run** button (or Shift + F10)
- Select your device
- App will install and launch

### 4. Grant Camera Permission
- When prompted, tap **"Allow"** for camera access
- This enables AI grading and QR scanning

---

## 📱 First Test Run

### Test 1: Log a Harvest
1. Tap **"Harvest"** tab at bottom
2. Enter quantity: `10.5`
3. Select floral source: **Coffee**
4. Select grade: **Medium**
5. Tap **"Add Harvest Log"**
6. ✅ See your log appear in the list below

### Test 2: Create a Batch
1. Tap **"Batches"** tab
2. Check the box next to your harvest log
3. Tap **"Create Batch"**
4. ✅ See batch created with auto-generated ID

### Test 3: View QR Code
1. On the batch card, tap the **QR icon**
2. ✅ See QR code displayed
3. Take a screenshot for testing

### Test 4: Scan QR Code
1. Go back to **"Home"** tab
2. Tap **"Scan QR Code"**
3. Point camera at the QR code (from your screenshot or another device)
4. ✅ See batch verified with full details

### Test 5: AI Grading
1. Go to **"Home"** tab
2. Tap **"AI Grade Check"**
3. Point camera at any honey-colored object
4. Tap the **capture button**
5. ✅ See classification result
   - Note: Will show "Using color analysis" if no TFLite model

### Test 6: Calculate Profit
1. Go to **"Home"** tab
2. Tap **"Profit Calculator"**
3. Enter:
   - Quantity: `10`
   - Cost per kg: `500`
   - Selling price: `800`
4. Tap **"Calculate"**
5. ✅ See profit: ₹3000 (₹300/kg, 37.5% margin)

---

## 🎯 Key Screens Overview

### Home Screen
- **Dashboard**: Total stock, breakdown by source
- **Quick Actions**: Navigate to all features

### Harvest Screen
- **Top**: Form to add new harvest log
- **Bottom**: List of all harvests (newest first)

### Batches Screen
- **Top**: Create batch from unused logs
- **Bottom**: List of batches with QR codes

### Camera Screen
- **Live Preview**: Camera view
- **Capture Button**: Takes photo and classifies
- **Result**: Grade with confidence

### QR Scanner Screen
- **Camera View**: Scans QR codes automatically
- **Result**: Shows batch details from database

### Profit Calculator
- **Input Fields**: Quantity, cost, selling price
- **Results**: Total cost, revenue, profit, margin

---

## 🔧 Common Issues

### Issue: App won't compile
**Solution**: File → Invalidate Caches → Restart

### Issue: Camera not working
**Solution**: 
- Grant camera permission
- Try on physical device (emulator cameras are limited)

### Issue: No data showing
**Solution**: Add some harvest logs first (data starts empty)

### Issue: Gradle sync failed
**Solution**: 
- Check internet connection
- Update Android Studio
- File → Sync Project with Gradle Files

---

## 📖 Code Structure (Quick Reference)

```
Key Files to Explore:
├── MainActivity.kt          # App entry point
├── data/
│   ├── entity/             # Database tables
│   ├── dao/                # Database queries
│   └── repository/         # Business logic
├── ui/
│   ├── screen/             # All UI screens
│   └── viewmodel/          # State management
├── ml/
│   └── HoneyGradeClassifier.kt  # AI grading
└── utils/
    └── QRCodeGenerator.kt       # QR handling
```

---

## 🎓 Next Steps

### For Learning:
1. Read `IMPLEMENTATION_NOTES.md` - Detailed architecture
2. Read `README.md` - Full documentation
3. Explore each screen's code
4. Try modifying colors in `Theme.kt`
5. Add a new field to harvest logs

### For Production:
1. Read `TFLITE_MODEL_GUIDE.md` - Add real ML model
2. Customize color scheme in `Theme.kt`
3. Add app icon and splash screen
4. Test on multiple devices
5. Generate signed APK

---

## 🛠️ Building Release APK

```bash
# Generate release APK
./gradlew assembleRelease

# APK location:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

For signed APK:
1. Build → Generate Signed Bundle/APK
2. Select APK
3. Create or use existing keystore
4. Choose release variant
5. Click Finish

---

## 🎨 Quick Customization

### Change App Name
Edit: `app/src/main/res/values/strings.xml`
```xml
<string name="app_name">Your App Name</string>
```

### Change Theme Colors
Edit: `app/src/main/java/com/jenugumpu/app/ui/theme/Theme.kt`
```kotlin
private val HoneyYellow = Color(0xFFFFC107)  // Change this
```

### Add New Floral Source
Edit: `app/src/main/java/com/jenugumpu/app/data/entity/HarvestLog.kt`
```kotlin
enum class FloralSource {
    COFFEE,
    WILDFLOWER,
    NEEM,
    MIXED,
    YOUR_NEW_SOURCE  // Add here
}
```

---

## ✅ Verification Checklist

- [ ] App compiles without errors
- [ ] All bottom nav items work
- [ ] Can add harvest log
- [ ] Can create batch
- [ ] Can view QR code
- [ ] Can scan QR code
- [ ] Camera preview works
- [ ] Calculator works correctly
- [ ] Stock totals update automatically

---

## 💡 Tips

1. **Use Real Device** - Emulator cameras are slow
2. **Check Logcat** - For debugging: View → Tool Windows → Logcat
3. **Rebuild Project** - If UI doesn't update: Build → Rebuild Project
4. **Clear Data** - To reset: Settings → Apps → Jenu-Gumpu → Clear Data

---

## 🆘 Need Help?

1. Check **`README.md`** for detailed docs
2. Check **`IMPLEMENTATION_NOTES.md`** for technical details
3. Check **logcat** for error messages
4. Verify all dependencies synced properly

---

**You're ready to go!** 🚀🍯

Try logging some harvests, creating batches, and scanning QR codes. The app is fully functional offline!
