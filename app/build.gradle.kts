plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
    id("kotlin-parcelize")
    id("maven-publish")
}
val libVersion = "1.1.5"
android {
    namespace = "com.koai.netlogger"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true
        aarMetadata {
            minCompileSdk = 29
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
val enableEditCore = project.findProperty("enableEditCore") == "true"
dependencies {
    if (enableEditCore) {
        implementation(project(":core"))
    } else {
        implementation(libs.basedev)
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.koai"
                artifactId = "netlogger"
                version = libVersion

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}

tasks.register("localBuild") {
    dependsOn("assembleRelease")
}

tasks.register("createReleaseTag") {
    doLast {
        val tagName = "v$libVersion"
        try {
            println("Creating tag: $tagName")

            providers
                .exec {
                    commandLine("git", "tag", "-a", tagName, "-m", "Release tag $tagName")
                }.result
                .get()

            providers
                .exec {
                    commandLine("git", "push", "origin", tagName)
                }.result
                .get()

            println("Successfully created and pushed tag: $tagName")
        } catch (e: Exception) {
            println("❌ Failed to create/push tag $tagName: ${e.message}")
        }
    }
}
/**
 * to build new version library: run in terminal
 *  ./gradlew cleanBuildPublish
 *
 */
tasks.register("cleanBuildPublish") {
    dependsOn("clean")
    dependsOn("localBuild")
    dependsOn("publishReleasePublicationToMavenRepository")
    val assembleReleaseTask = getTasksByName("localBuild", false).stream().findFirst().orElse(null)
    if (assembleReleaseTask != null) {
        assembleReleaseTask.mustRunAfter("clean")
        assembleReleaseTask.finalizedBy("publishReleasePublicationToMavenRepository")
    }
}
