import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
}

allprojects {
    val properties = gradleLocalProperties(rootDir)
    ext {
        set("signing.keyId", properties["signing.keyId"])
        set("signing.password", properties["signing.password"])
        set("signing.secretKeyRingFile", properties["signing.secretKeyRingFile"])
    }
}

task("clean"){
    delete(rootProject.buildDir)
}