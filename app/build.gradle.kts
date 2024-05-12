import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
//    jacoco
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.23"
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
//    id("com.dicedmelon.gradle.jacoco-android") version "0.1.5"
    jacoco
}


task<JacocoReport>("mergedJacocoReport") {

    dependsOn("testDebugUnitTest", "createDebugCoverageReport")

    reports {

    }

    val fileFilter = mutableSetOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )
    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/kotlin"

    sourceDirectories = files(mainSrc)
    classDirectories = files(debugTree)
    executionData = fileTree(project.buildDir) {
        include(
            "jacoco/testDebugUnitTest.exec",
            "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
        )
    }
}


android {
    namespace = "com.example.sharedcalendar"
    compileSdk = 34



    testOptions {
        unitTests {
//            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    defaultConfig {
        applicationId = "com.example.sharedcalendar"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArgument(
            "runnerBuilder", "de.mannodermaus.junit5.AndroidJUnit5Builder"
        )
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
//            enableAndroidTestCoverage = true
//            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }



    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.withType(Test::class) {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    // config JVM target to 1.8 for kotlin compilation tasks
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

}
//val exclusions = listOf(
//    "**/R.class",
//    "**/R\$*.class",
//    "**/BuildConfig.*",
//    "**/Manifest*.*",
//    "**/*Test*.*"
//)
dependencies {
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("com.google.accompanist:accompanist-adaptive:0.35.0-alpha")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material3:material3-android:1.2.1")

    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test:core-ktx:1.6.0-alpha06")
    androidTestImplementation(project(":app"))
    debugImplementation("androidx.fragment:fragment-testing:1.7.0")
    androidTestImplementation("androidx.test:runner:1.5.2")

    androidTestImplementation("androidx.test:core-ktx:1.2.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    androidTestImplementation("com.google.truth:truth:0.44")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.12")

    val mockkVersion = "1.13.10"
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.mockk:mockk-android:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-agent:${mockkVersion}")

    testImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.android.support.test.espresso:espresso-contrib:3.0.2")

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    // (Optional) If you need "Parameterized Tests"
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    // (Optional) If you also have JUnit 4-based tests
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.0")

    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")

    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)

//
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.kizitonwose.calendar:view:2.5.0")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
    implementation("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:2.0.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material:material:1.6.7")
    implementation("androidx.compose.foundation:foundation:1.6.7")


    // Coroutine (For API)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")

    implementation("com.github.crysxd:shared-firebase-preferences:1.0.1")

    implementation("com.github.thellmund.Android-Week-View:core:5.2.4")
    implementation("com.github.thellmund.Android-Week-View:jsr310:5.2.4")
    implementation("com.github.thellmund.Android-Week-View:emoji:5.2.4")
}


