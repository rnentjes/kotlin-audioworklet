import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target.VAR
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType.LEGACY
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType.IR
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.20"
    application
}

group = "nl.astraeus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

val jsMode = IR

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(jsMode) {
        binaries.executable()
        browser()
    }
    js("jsAudioWorklet", jsMode) {
        binaries.executable()

        browser {
            commonWebpackConfig {
                outputFileName = "audio-worklet.js"
            }

            webpackTask {
                output.libraryTarget = VAR
                // set library name, so we can call kotlin generated code from javascript
                output.library = "audioWorklet"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:2.0.2")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
            }
        }
        val jsMain by getting
        val jsAudioWorkletMain by getting
    }
}

application {
    mainClass.set("nl.astraeus.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
    from(tasks.named("jsAudioWorkletBrowserDistribution"))
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}