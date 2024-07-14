# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-keepattributes *Annotation*

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}
-keepclassmembers class com.lapakkreatiflamongan.** {
    public *;
    protected *;
}

-keep class com.google.** { *; }
-keep class com.squareup.** { *; }
-keep class com.github.** { *; }
-keep class com.andremion.** { *; }
-keep class com.evrencoskun.** { *; }
-keep class com.daimajia.** { *; }
-keep class com.jaredrummler.** { *; }
-keep class com.libizo.** { *; }
-keep class com.loopj.** { *; }
-keep class com.nex3z.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class com.ogaclejapan.** { *; }
-keep class com.oguzdev.** { *; }
-keep class com.scottyab.** { *; }
-keep class com.pnikosis.** { *; }
-keep class com.shuhart.** { *; }
-keep class com.tapadoo.** { *; }
-keep class com.txusballesteros.** { *; }
-keep class android.** { *; }
-keep class androidx.** { *; }
-keep class io.** { *; }
-keep class me.** { *; }
-keep class de.** { *; }
-keep class us.** { *; }
-keep class cn.** { *; }

