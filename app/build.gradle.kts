import java.util.Locale
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  jacoco
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

    testInstrumentationRunner = "com.waffiq.testactivity.testrunner.CustomTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    debug {
      enableAndroidTestCoverage = true
      enableUnitTestCoverage = true
    }
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlin {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_17
    }
  }
  buildFeatures {
    compose = true
  }
  @Suppress("UnstableApiUsage")
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
  ksp(libs.kotlin.metadata.jvm)
  ksp(libs.hilt.compiler)

  // dagger hilt test
  androidTestImplementation(libs.ui.test.junit4)
  androidTestImplementation(libs.hilt.android.testing)
  kspAndroidTest(libs.hilt.android.compiler)

  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  testImplementation(libs.junit)
  testImplementation(libs.turbine)
  testImplementation(libs.kotlinx.coroutines.test)
}

val exclusions = listOf(
  "**/R.class",
  "**/R\$*.class",
  "**/BuildConfig.*",
  "**/Manifest*.*",
  "**/*Test*.*",
  "**/hilt_aggregated_deps/**",
  "**/*_Provide*",
  "**/*_Factory*",
  "**/*Preview*", // Preview classes
  "**/*_*",// Auto generated classes
  "**/di" // <-------- di modules
)

tasks.withType(Test::class) {
  configure<JacocoTaskExtension> {
    isIncludeNoLocationClasses = true
    excludes = listOf("jdk.internal.*")
  }
}

android {
  applicationVariants.all(closureOf<com.android.build.gradle.internal.api.BaseVariantImpl> {
    val variant = this@closureOf.name.replaceFirstChar {
      if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
      ) else it.toString()
    }

    val unitTests = "test${variant}UnitTest"
    val androidTests = "connected${variant}AndroidTest"

    tasks.register<JacocoReport>("Jacoco${variant}CodeCoverage") {
      dependsOn(listOf(unitTests, androidTests))
      //dependsOn(listOf(unitTests))
      group = "Reporting"
      description = "Execute ui and unit tests, generate and combine Jacoco coverage report"
      reports {
        xml.required.set(true)
        html.required.set(true)
      }
      sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
      classDirectories.setFrom(files(
        fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
          exclude(exclusions)
        },
        fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
          exclude(exclusions)
        }
      ))
      executionData.setFrom(files(
        fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
      ))
    }
  })
}
