import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.io.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

version = "1.0.6"
val iOSBinaryName = "shared"

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
            baseName = "shared"
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
    applyDefaultHierarchyTemplate()
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
tasks.register("AprepareSharedFrameworks") {
    description = "Publish iOS framework to the Cocoa Repo"

    // Create Release Framework for Xcode
    dependsOn("assembleXCFramework", "packageDistribution")

    doLast {
        // Update Podspec Version
        val poddir = File("$rootDir/$iOSBinaryName.podspec")
        val podtempFile = File("$rootDir/$iOSBinaryName.podspec.new")
        val gitBranch = gitBranch()
        logger.debug("*****************Unable to determine current branch: ${gitBranch}")
        val podreader = poddir.bufferedReader()
        val podwriter = podtempFile.bufferedWriter()
        var podcurrentLine: String?

        while (podreader.readLine().also { currLine -> podcurrentLine = currLine } != null) {
            if (podcurrentLine?.trim()?.startsWith("spec.version") == true) {
                podwriter.write("    spec.version       = \"${version}\"" + System.lineSeparator())
            } else if (podcurrentLine?.trim()?.startsWith("spec.source") == true) {
                podwriter.write("    spec.source       = { :http => \"https://github.com/mzfkr97/SharedLibrary/tree/${iOSBinaryName}.xcframework.zip\" :branch => \"${gitBranch()}\" }" + System.lineSeparator())
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
    delete("$rootDir/XCFramework")
    copy {
        from("$buildDir/XCFrameworks/release")
        into("$rootDir/XCFramework")
    }
    delete("$rootDir/${iOSBinaryName}.xcframework.zip")
    archiveFileName.set("${iOSBinaryName}.xcframework.zip")
    destinationDirectory.set(layout.projectDirectory.dir("../"))
    from(layout.projectDirectory.dir("../XCFramework"))
}

tasks.register<DefaultTask>("uploadFileToGit") {
    doLast {
        val gitUrl = "https://github.com/mzfkr97/SharedLibrary/"
        project.exec {
            commandLine("git", "add", "$rootDir/${iOSBinaryName}.xcframework.zip")
            commandLine("git", "commit", "-m", "Add file via Gradle task")
            commandLine("git", "push", gitUrl)
        }
    }
}

tasks.register("AcreateGitHubFolder") {
    doLast {
        val folderName = "newFolder" // Замените на имя вашей папки
        val localFolderPath = "$projectDir/$folderName" // Путь к папке в вашем проекте
        val fileName = ".gitkeep" // Имя файла для добавления в папку
        // Создание папки и файла внутри неё
        File(localFolderPath).apply {
            mkdirs()
            File(this, fileName).createNewFile()
        }
        project.exec {
            commandLine("git", "add", "$folderName/$fileName")
            commandLine("git", "commit", "-m", "Add file via Gradle task")
            commandLine("git", "push")
        }
    }
}

fun gitBranch(): String {
    return try {
        val byteOut = ByteArrayOutputStream()
        project.exec {
            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
            standardOutput = byteOut
        }
        String(byteOut.toByteArray()).trim().also {
            if (it == "HEAD")
                logger.warn("Unable to determine current branch: Project is checked out with detached head!")
        }
    } catch (e: Exception) {
        logger.warn("Unable to determine current branch: ${e.message}")
        "Unknown Branch"
    }
}

