import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
    signing
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.11.0"
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
//        version = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxCompose.get()
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

//val sourcesJar by tasks.registering(Jar::class) {
//    archiveClassifier.set("sources")
//    from(android.sourceSets.getByName("main").java.srcDirs)
//}

fun getPropertyFromProject(key: String) : String{
    return project.property(key).toString()
}

afterEvaluate {
    publishing {
        publications {
//            create<MavenPublication>("debug") {
//                groupId = getPropertyFromProject("GROUP_ID")
//                artifactId = getPropertyFromProject("ARTIFACT_ID")
//                version = getPropertyFromProject("VERSION_NAME") + "-SNAPSHOT"
//                from(components["release"])
//            }
            create<MavenPublication>("release") {
                groupId = getPropertyFromProject("GROUP_ID")
                artifactId = getPropertyFromProject("ARTIFACT_ID")
                version = getPropertyFromProject("VERSION_NAME")
                from(components["release"])

                pom {
                    name.set(getPropertyFromProject("ARTIFACT_ID"))
                    description.set(getPropertyFromProject("POM_DESCRIPTION"))
                    url.set(getPropertyFromProject("POM_URL"))

                    licenses {
                        license {
                            name.set(getPropertyFromProject("POM_LICENCE_NAME"))
                            url.set(getPropertyFromProject("POM_LICENCE_URL"))
                        }
                    }
                    developers {
                        developer {
                            id.set(getPropertyFromProject("POM_DEVELOPER_ID"))
                            name.set(getPropertyFromProject("POM_DEVELOPER_NAME"))
                            email.set(getPropertyFromProject("POM_DEVELOPER_URL"))
                        }
                    }
                    scm {
                        connection.set(getPropertyFromProject("POM_SCM_CONNECTION"))
                        developerConnection.set(getPropertyFromProject("POM_SCM_DEV_CONNECTION"))
                        url.set(getPropertyFromProject("POM_SCM_URL"))
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (project.hasProperty("release")) releasesRepoUrl else releasesRepoUrl
                credentials {
                    val properties = gradleLocalProperties(rootDir)
                    username = properties["mavenCentralUsername"] as String?
                    password = properties["mavenCentralPassword"] as String?
                }
            }
        }

    }

//    signing {
//        sign(publishing.publications)
//    }
}

dependencies {
    implementation(libs.bundles.compose.base.lib)
    api(libs.bundles.paging)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}