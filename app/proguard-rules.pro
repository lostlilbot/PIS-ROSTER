# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Room entities
-keep class com.pisroster.app.data.entity.** { *; }

# Keep data classes
-keep class com.pisroster.app.data.model.** { *; }

# OpenCSV
-keep class com.opencsv.** { *; }
-dontwarn com.opencsv.**

# iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**
