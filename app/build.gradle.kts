plugins {
    alias(libs.plugins.application.android)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.serialization.ktx)
    alias(libs.plugins.gms)
}

android {
    namespace = "io.github.livenlearnaday.firebaseauth"
    compileSdk = libs.versions.android.compileSdk.get().toInt()


    defaultConfig {
        applicationId = "io.github.livenlearnaday.firebaseauth"
        minSdk = libs.versions.android.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        /* Retrieves API from local.properties */
        val properties = org.jetbrains.kotlin.konan.properties.Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "AUTH_WEB_CLIENT_ID",
            properties.getProperty("AUTH_WEB_CLIENT_ID")
        )
    }

    signingConfigs {
        create("release") {
            this.keyAlias = "key0"
            this.keyPassword = "${properties["KEY_PASSWORD"]}"
            this.storeFile = file("_files/_keystores/firebaseauth.jks")
            this.storePassword = "${properties["STORE_PASSWORD"]}"
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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.get()
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
    }

}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Timber logging
    implementation(libs.timber)

    // coroutines
    implementation(libs.coroutines)

    // koin di
    implementation(libs.bundles.koin)
    implementation(libs.bundles.koin.compose)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid.identity)

    // navigation
    implementation(libs.bundles.voyager)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.glide.compose)

    //Test
    testImplementation(libs.bundles.test.impl)
    androidTestImplementation(libs.bundles.android.test.impl)
    androidTestImplementation(libs.bundles.navigation)
    debugImplementation(libs.bundles.debug.impl)

    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

}