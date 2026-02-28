# Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Keep Kotlin metadata for Compose
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Keep game engine (pure logic, used via reflection-free paths)
-keep class com.tructt.caro.domain.** { *; }

# Keep ViewModel
-keep class com.tructt.caro.presentation.** { *; }
