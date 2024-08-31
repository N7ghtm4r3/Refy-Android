import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.tecknobit.refy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tecknobit.refy"
        minSdk = 31
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "*/**"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.refycore)
    implementation(libs.coil.compose)
    implementation(libs.richeditor.compose)
    implementation(libs.equinox)
    implementation(libs.colorpicker.compose)
    implementation(libs.apimanager)
    implementation(libs.review)
    implementation(libs.review.ktx)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation("com.github.N7ghtm4r3:Equinox-Compose:1.0.0") {
        exclude("com.github.N7ghtm4r3.Equinox-Compose", "library-jvm")
    }
    dokkaPlugin(libs.android.documentation.plugin)
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Refy")
            moduleVersion.set(android.defaultConfig.versionName)
            outputDirectory.set(layout.projectDirectory.dir("../docs"))
        }
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("../docs/logo-icon.svg"))
        footerMessage = "(c) 2024 Tecknobit"
    }
}

configurations.all {
    exclude("commons-logging", "commons-logging")
}