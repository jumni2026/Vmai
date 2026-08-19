# ============================================================
# VMAX Enterprise v2.6
# app/proguard-rules.pro
# R8 / ProGuard configuration
# ============================================================

# ------------------------------------------------------------
# Android framework components
# ------------------------------------------------------------

# AccessibilityService is instantiated by the Android framework.
-keep public class com.vmax.app.VMAXAccessibilityService {
    public <init>();
}

# Application class is instantiated by Android.
-keep public class com.vmax.app.VMAXApplication {
    public <init>();
}

# ------------------------------------------------------------
# Android components referenced from AndroidManifest.xml
# ------------------------------------------------------------

-keep public class com.vmax.app.MainActivity {
    public <init>();
}

# ------------------------------------------------------------
# Parcelable CREATOR fields
# ------------------------------------------------------------

-keepclassmembers class * implements android.os.Parcelable {
    public static ** CREATOR;
}

# ------------------------------------------------------------
# Serializable classes
# ------------------------------------------------------------

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ------------------------------------------------------------
# Kotlin metadata
# ------------------------------------------------------------

-keep class kotlin.Metadata { *; }

# ------------------------------------------------------------
# Keep enum values/names where required by reflection or
# serialized state.
# ------------------------------------------------------------

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ------------------------------------------------------------
# Suppress harmless warnings from optional Java/Kotlin classes.
# ------------------------------------------------------------

-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**

# ============================================================
# End of VMAX Enterprise R8 configuration
# ============================================================
