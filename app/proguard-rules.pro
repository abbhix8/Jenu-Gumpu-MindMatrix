# Add project specific ProGuard rules here.
-keep class com.jenugumpu.app.** { *; }
-keepclassmembers class com.jenugumpu.app.data.entity.** { *; }

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
