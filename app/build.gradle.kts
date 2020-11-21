plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.apollographql.apollo").version("2.4.2")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "com.android.squadster"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL_SQUADSTER_AUTH", "\"http://squadster.wtf/api/auth/vk?state=mobile=true\"")
            buildConfigField("String", "BASE_URL_SQUADSTER_QUERY", "\"http://squadster.wtf/api/query\"")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    androidExtensions {
        isExperimental  = true
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    lintOptions {
        lintConfig = file("$project.rootDir/build_config/internal_lint.xml")
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.10")
    kapt("com.github.moxy-community:moxy-compiler:2.2.0")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:3.1.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.3.0-alpha02")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Moxy Community(MVP)
    implementation("com.github.moxy-community:moxy-androidx:2.2.0")
    implementation("com.github.moxy-community:moxy:2.2.0")

    // Toothpick (DI)
    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:3.1.0")
    implementation("com.github.stephanenicolas.toothpick:smoothie-androidx:3.1.0")

    // Cicerone (Navigation)
    implementation("ru.terrakok.cicerone:cicerone:5.1.1")

    // RxJava (Async)
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Retrofit (Network)
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.7.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.0")

    // Material Components
    implementation("com.google.android.material:material:1.2.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")

    // Apollo
    implementation("com.apollographql.apollo:apollo-runtime:2.4.2")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.2")

    //Coroutine
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
}

apollo {
    generateKotlinModels.set(true)
}