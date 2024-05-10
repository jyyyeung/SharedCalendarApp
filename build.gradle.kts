// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
    kotlin("jvm") version "1.9.23" apply false
}


//
//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.2"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}
//
//tasks.test {
//    useJUnitPlatform()
//    testLogging {
//        events("passed", "skipped", "failed")
//    }
//}
//
//tasks.withType<JavaCompile>().configureEach {
//    options.release.set(8)
//}
//
//// config JVM target to 1.8 for kotlin compilation tasks
//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions.jvmTarget = "1.8"
//}