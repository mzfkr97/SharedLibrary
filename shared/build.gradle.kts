import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

version = "1.0.2"
val iOSBinaryName = "sharedRoman"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sharedRoman"
            xcf.add(this)
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
    }
}

android {
    namespace = "com.romanzhurid.sharedlibrary"
    compileSdk = 34
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


/**
 * This task prepares Release Artefact for iOS XCFramework
 * Updates Podspec, Package.swift, Carthage contents with version and checksum
 */
tasks.register("prepareSharedFramework") {
    description = "Publish iOS framework to the Cocoa Repo"

    // Create Release Framework for Xcode
    dependsOn("assembleXCFramework", "packageDistribution")

    doLast {
        // Update Podspec Version
        val poddir = File("$rootDir/$iOSBinaryName.podspec")
        val podtempFile = File("$rootDir/$iOSBinaryName.podspec.new")

        val podreader = poddir.bufferedReader()
        val podwriter = podtempFile.bufferedWriter()
        var podcurrentLine: String?

        while (podreader.readLine().also { currLine -> podcurrentLine = currLine } != null) {
            if (podcurrentLine?.trim()?.startsWith("spec.version") == true) {
                podwriter.write("    spec.version       = \"${version}\"" + System.lineSeparator())
            } else if (podcurrentLine?.trim()?.startsWith("spec.source") == true) {
                podwriter.write("    spec.source       = { :http => \"https://github.com/mzfkr97/SharedLibrary/releases/download/${version}/${iOSBinaryName}.xcframework.zip\"}" + System.lineSeparator())
            } else {
                podwriter.write(podcurrentLine + System.lineSeparator())
            }
        }
        podwriter.close()
        podreader.close()
        podtempFile.renameTo(poddir)
    }
}

tasks.create<Zip>("packageDistribution") {
    // Delete existing XCFramework
    delete("$rootDir/XCFramework")

    // Replace XCFramework File at root from Build Directory
    copy {
        from("$buildDir/XCFrameworks/release")
        into("$rootDir/XCFramework")
    }

    // Delete existing ZIP, if any
    delete("$rootDir/${iOSBinaryName}.xcframework.zip")
    // ZIP File Name - as per Carthage Nomenclature
    archiveFileName.set("${iOSBinaryName}.xcframework.zip")
    // Destination for ZIP File
    destinationDirectory.set(layout.projectDirectory.dir("../"))
    // Source Directory for ZIP
    from(layout.projectDirectory.dir("../XCFramework"))
}