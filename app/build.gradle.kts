
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs")
}
android {
    namespace = "com.example.fakecall"
    compileSdk = 34

    defaultConfig {
        applicationId =
            "com.fakecall.prankfriend.fakechat.fakevideocall.celebrityfakecall.prankchat.fakecall"
        minSdk = 28
        targetSdk = 34
        versionCode = 41
        versionName = "1.41"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        base.archivesBaseName = "Fake_Video_&_Audio_Call" + versionName + "(AppSoftStudio)"


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        buildConfig = true
    }
    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.databinding:databinding-runtime:8.3.2")
    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.compose.ui:ui-test-android:1.7.8")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    implementation ("androidx.lifecycle:lifecycle-process:2.8.4")

    //sdplibrary
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    //permissionx
//inappreview and unupdate
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.github.mindinventory:minavdrawer:1.2.2")
//circular image library
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //glide library
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.camera:camera-camera2:1.0.0-beta07")
    implementation("androidx.camera:camera-lifecycle:1.0.0-beta07")
    implementation("androidx.camera:camera-view:1.0.0-alpha14")
    //lottie
    implementation("com.airbnb.android:lottie:6.3.0")
//call library
    implementation("com.github.mhdmoh:swipe-button:1.0.3")
    //ads
    implementation("com.google.android.gms:play-services-ads:24.4.0")
//    implementation("com.google.android.gms:play-services-ads:23.6.0")
    //sdk for GDPR
    implementation("com.google.android.ump:user-messaging-platform:2.1.0")
    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    //billing
    implementation("com.android.billingclient:billing-ktx:7.0.0")
    //inappupdate
    implementation("com.google.android.play:app-update:2.1.0")
// For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("com.google.android.play:review:2.0.1")
    // For Kotlin users also import the Kotlin extensions library for Play In-App Review:
    implementation("com.google.android.play:review-ktx:2.0.1")

    implementation("com.google.ads.mediation:applovin:13.3.1.0")
    implementation("com.google.ads.mediation:facebook:6.20.0.0")

}