import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.api.variant.SourceDirectories
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
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
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
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
  "**/Composable*.*",
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

androidComponents {
  onVariants { variant ->
    val variantName = variant.name.replaceFirstChar { it.uppercase() }
    val unitTests = "test${variantName}UnitTest"
    val androidTests = "connected${variantName}AndroidTest"

    val myObjFactory = project.objects
    val buildDir = layout.buildDirectory.get().asFile
    val allJars: ListProperty<RegularFile> = myObjFactory.listProperty(RegularFile::class.java)
    val allDirectories: ListProperty<Directory> = myObjFactory.listProperty(Directory::class.java)
    val reportTask = tasks.register<JacocoReport>("Jacoco${variantName}CodeCoverage") {
      dependsOn(listOf(unitTests, androidTests))

      classDirectories.setFrom(
        allJars.map { jars ->
          jars.filter { jar ->
            val jarName = jar.asFile.name.lowercase()
            jarName.contains("classes.jar") &&
              !jarName.contains("androidx") &&
              !jarName.contains("android.jar") &&
              !jarName.contains("kotlin-stdlib") &&
              !jarName.contains("kotlinx-") &&
              !jarName.contains("hilt-") &&
              !jarName.contains("dagger-")
          }.map { jar ->
            zipTree(jar).matching {
              exclude(exclusions)
            }
          }
        },
        allDirectories.map { dirs ->
          dirs.map { dir ->
            myObjFactory.fileTree().setDir(dir).exclude(exclusions)
          }
        },
      )

      reports {
        xml.required = true
        html.required = true
      }

      fun SourceDirectories.Flat?.toFilePaths(): Provider<List<String>> =
        this?.all?.map { directories -> directories.map { it.asFile.path } }
          ?: provider { emptyList() }
      sourceDirectories.setFrom(
        files(
          variant.sources.java.toFilePaths(), variant.sources.kotlin.toFilePaths()
        ),
      )

      executionData.setFrom(
        project.fileTree("$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest")
          .matching { include("**/*.exec") },

        project.fileTree("$buildDir/outputs/code_coverage/${variant.name}AndroidTest")
          .matching { include("**/*.ec") },
      )
    }


    variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT).use(reportTask).toGet(
      ScopedArtifact.CLASSES,
      { _ -> allJars },
      { _ -> allDirectories },
    )
  }
}
