import org.jetbrains.kotlin.config.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose)
  id("kotlin-kapt")
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.waffiq.testactivity"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.waffiq.testactivity"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner  = "com.waffiq.testactivity.testrunner.CustomTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = JvmTarget.JVM_11.description
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get().toString()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.activity.compose)
  implementation(libs.ui)
  implementation(libs.ui.tooling.preview)
  implementation(libs.material3)

  // dagger hilt
  implementation(libs.hilt.android)
  implementation(libs.hilt.navigation.compose)
  kapt(libs.hilt.compiler)

  // dagger hilt test
  androidTestImplementation(libs.ui.test.junit4)
  androidTestImplementation(libs.hilt.android.testing)
  kaptAndroidTest(libs.hilt.android.compiler)

  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}
