plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // --- INICIO DE CAMBIOS ---
    // Aplica el plugin KSP
    alias(libs.plugins.ksp)
    // --- FIN DE CAMBIOS ---
}

android {
    namespace = "com.example.appagendita_grupo1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appagendita_grupo1"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- INICIO DE CAMBIOS (DEPENDENCIAS DE ROOM) ---
    implementation(libs.androidx.room.runtime) // Runtime de Room
    implementation(libs.androidx.room.ktx)     // Extensiones Kotlin (Coroutines/Flow)
    ksp(libs.androidx.room.compiler)            // Procesador de anotaciones (KSP)
    // --- FIN DE CAMBIOS ---

    // Security Crypto for encrypted SharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    
    // BCrypt for password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    // Retrofit para llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}