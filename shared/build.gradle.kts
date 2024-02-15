import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.io.ByteArrayOutputStream


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    //id("io.github.donadev.kmm.ios_deploy.plugin") version "0.0.20"
}

version = "1.0.29"
val iOSBinaryName = "shared"
val IOS_PUBLISHING = "ios_publishing"

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

tasks.create<Zip>("packageDistribution") {
    group = IOS_PUBLISHING
    val localFolderPath = "$projectDir/releases/$version"
    delete("$rootDir/XCFramework")
    copy {
        from("$buildDir/XCFrameworks/release")
        into("$rootDir/XCFramework")
    }
    delete("$rootDir/${iOSBinaryName}.xcframework.zip")
    archiveFileName.set("${iOSBinaryName}.xcframework.zip")
    //destinationDirectory.set(layout.projectDirectory.dir("../"))
    destinationDirectory.set(layout.projectDirectory.dir(localFolderPath))
    from(layout.projectDirectory.dir("../XCFramework"))
}

val AAcreateGitHubFolder: TaskProvider<Task> by tasks.registering {
    group = IOS_PUBLISHING
    doLast {
        val folderName = "releases"
        val dir = file("$projectDir/$folderName/$version")
        if (!dir.exists()) {
            dir.mkdirs()
        } else {
            logger.lifecycle("Каталог '${dir.absolutePath}' уже существует.")
        }
//        project.exec {
//            commandLine("git", "add", "$projectDir/$folderName/$version")
//
//            //commandLine = "git add $localFolderPath".split(" ")
//            //commandLine = "git commit -m"
//            //commandLine = "git push".split(" ")
////            commandLine("git", "add", "$folderName/$fileName")
////            commandLine("git", "commit", "-m", "Add file via Gradle task")
////            commandLine("git", "push")
//        }
    }
}

tasks.register("AAAcommitChanges") {
    group = IOS_PUBLISHING
    description = "Commits all changes with a default commit message."

    val gitStatusOutput = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "status", "--porcelain")
        standardOutput = gitStatusOutput
    }
    if (gitStatusOutput.toString().isBlank()) {
        println("***************No changes to commit.")
    } else {
        exec {
            commandLine = listOf("git", "add", ".")
        }
        exec {
            commandLine = listOf("git", "commit", "-m", "Commit changes")
        }
    }
}

val gitStatus by tasks.registering(Exec::class) {
    group = IOS_PUBLISHING
    commandLine("git", "status", "--porcelain")
    standardOutput = ByteArrayOutputStream()
    logger.lifecycle("****${standardOutput.toString().trim().isNotEmpty()}")
}

val gitCommit by tasks.registering(Exec::class) {
    group = IOS_PUBLISHING
    //dependsOn("assembleXCFramework", "packageDistribution")

    onlyIf { gitStatus.get().standardOutput.toString().trim().isNotEmpty() }
    doLast {
        exec {
            commandLine = listOf("git", "add", "$projectDir/releases/1.0.29")
        }
        exec {
            commandLine = listOf("git", "commit", "-am", "Commit changes")
        }
    }
}

val getCurrentPublishedPodVersion by tasks.registering {
    group = IOS_PUBLISHING

    doLast {
        val podName = "sharedLibraryZhurid"
        val outputStream = ByteArrayOutputStream()
        val process = ProcessBuilder("pod", "search", podName, "--simple", "--no-ansi")
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectErrorStream(true)
            .start()
        process.inputStream.copyTo(outputStream)
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Execution of pod search failed with exit code $exitCode")
        }
        val result = outputStream.toString()
        val versionLine = result.lines().find { it.contains(podName) }
        val version = versionLine?.substringAfter("$podName (")?.substringBefore(")")

        logger.lifecycle(
            version?.let {
                "Current published version of $podName is: $version"
            } ?: run {
                "Unable to find the current published version of $podName."
            }
        )
    }
}

val publishPod by tasks.registering {
    group = IOS_PUBLISHING
    dependsOn(updatePodSpec, pushPod)
}

val pushPod by tasks.registering {
    group = IOS_PUBLISHING
    doLast {
        val process = ProcessBuilder(
            "pod", "trunk", "push", "sharedLibraryZhurid.podspec", "--allow-warnings"
        ).apply {
            directory(project.rootDir)
            redirectErrorStream(true)
        }.start()

        process.inputStream.bufferedReader().use {
            it.lines().forEach { line -> println(line) }
        }
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("pushPod Failed Execution of pod trunk push failed with exit code $exitCode")
        }
    }
}

val updatePodSpec by tasks.registering {
    group = IOS_PUBLISHING
    val podspec = """
            Pod::Spec.new do |spec|
                spec.name                     = 'sharedLibraryZhurid'
                spec.version                  = '${project.version}'
                spec.homepage                 = 'https://github.com/mzfkr97/SharedLibrary'
                spec.source       = { :http => "https://github.com/mzfkr97/SharedLibrary/raw/master/shared/releases/${project.version}/shared.xcframework.zip" }
                spec.authors                  = 'mzfkr97'
                spec.license                  = 'https://opensource.org/licenses/Apache-2.0'
                spec.summary                  = 'SharedLibrary summary'
                spec.vendored_frameworks      = "shared.xcframework"
                spec.libraries                = "c++"
                spec.static_framework         = true
                spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
                spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
                spec.ios.deployment_target = '11.0'
            end
        """.trimIndent()
    val outFile = File(project.rootDir, "sharedLibraryZhurid.podspec") //"${project.name}.podspec"
    outFile.writeText(podspec)

    logger.lifecycle("Pod: ${podspec}")
}
