#---------------------------------1.实体类---------------------------------

-keep class me.leefeng.beida.bean.** { *; }
-keep class me.leefeng.beida.dbmodel.** { *; }

#-------------------------------------------------------------------------

#---------------------------------Bugly---------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#---------------------------------支付SDK---------------------------------
-dontwarn com.unicom.**
-keep class com.unicom.**{*;}
-dontwarn com.sun.**
-keep class com.sun.**{*;}
#---------------------------------数据库操作liteorm---------------------------------

-dontwarn com.litesuits.orm.**
-keep class com.litesuits.orm.**{*;}
#---------------------------------播放器---------------------------------
-dontwarn tv.danmaku.ijk.**
-keep class tv.danmaku.ijk.** { *; }
#---------------------------------广告---------------------------------
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}
-keep class android.support.v4.**{ *;}
#---------------------------------小米推送---------------------------------
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**
-keep class  me.leefeng.beida.MiMessageReceiver{*;}
#---------------------------------Glide---------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#---------------------------------广告---------------------------------
######--butterknife--#######
-keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
  @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
 @butterknife.* <methods>;
 }

#### 短信验证 ####
 -keep class cn.smssdk.**{*;}
 -keep class com.mob.**{*;}
 -dontwarn com.mob.**
 -dontwarn cn.smssdk.**

#### 后端云 ####
 -ignorewarnings

 -keepattributes Signature,*Annotation*

 # keep BmobSDK
 -dontwarn cn.bmob.v3.**
 -keep class cn.bmob.v3.** {*;}

 # 确保JavaBean不被混淆-否则gson将无法将数据解析成具体对象
 -keep class * extends cn.bmob.v3.BmobObject {
     *;
 }
 -keep class com.example.bmobexample.bean.BankCard{*;}
 -keep class com.example.bmobexample.bean.GameScore{*;}
 -keep class com.example.bmobexample.bean.MyUser{*;}
 -keep class com.example.bmobexample.bean.Person{*;}
 -keep class com.example.bmobexample.file.Movie{*;}
 -keep class com.example.bmobexample.file.Song{*;}
 -keep class com.example.bmobexample.relation.Post{*;}
 -keep class com.example.bmobexample.relation.Comment{*;}

 # keep BmobPush
 -dontwarn  cn.bmob.push.**
 -keep class cn.bmob.push.** {*;}

 # keep okhttp3、okio
 -dontwarn okhttp3.**
 -keep class okhttp3.** { *;}
 -keep interface okhttp3.** { *; }
 -dontwarn okio.**

 # keep rx
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
  long producerIndex;
  long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 # 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}
 #### butterknife ####
    -keep class butterknife.** { *; }
      -dontwarn butterknife.internal.**
      -keep class **$$ViewBinder { *; }

      -keepclasseswithmembernames class * {
          @butterknife.* <fields>;
      }

      -keepclasseswithmembernames class * {
          @butterknife.* <methods>;
      }
      #####FastJson####
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}
-keep class **$Properties
#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------