import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

version = "1.0.7"
val iOSBinaryName = "shared"

val aACreateFileTask by tasks.registering(AACreateFileTask::class)
aACreateFileTask.configure {
    actions.clear()
    localFolderPath = "$projectDir/releases/$version"
}

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
tasks.getByName("aACreateFileTask")

val aaprepareSharedFrameworks: TaskProvider<Task> by tasks.registering {
    description = "Publish iOS framework to the Cocoa Repo"

    dependsOn("assembleXCFramework", "packageDistribution")

    doFirst {
        aACreateFileTask.get()
    }
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
                podwriter.write("    spec.source       = { :http => \"https://github.com/mzfkr97/SharedLibrary/tree/${iOSBinaryName}.xcframework.zip\" }" + System.lineSeparator())
            } else {
                podwriter.write(podcurrentLine + System.lineSeparator())
            }
        }
        podwriter.close()
        podreader.close()
        podtempFile.renameTo(poddir)
    }
//    project.exec {
//        commandLine("git", "add", localFolderPath)
//    }
}

tasks.create<Zip>("packageDistribution") {
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
    doLast {
        val folderName = "releases"
        val dir = file("$projectDir/$folderName/$version")
        if (!dir.exists()) {
            dir.mkdirs()
        } else {
            println("Каталог '${dir.absolutePath}' уже существует.")
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

//fun gitBranch(): String =
//    runCatching {
//        val byteOut = ByteArrayOutputStream()
//        project.exec {
//            commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
//            standardOutput = byteOut
//        }
//        String(byteOut.toByteArray()).trim().also {
//            if (it == "HEAD") {
//                logger.warn("Unable to determine current branch: Project is checked out with detached head!")
//            }
//        }
//    }.getOrElse { error ->
//        logger.warn("Unable to determine current branch: ${error.message}")
//        "master"
//    }

abstract class AACreateFileTask : DefaultTask() {

    @get:Input
    abstract val localFolderPath: Property<String>

    @TaskAction
    fun action() {

//        project.tasks.assemble.dependsOn(
//            project.tasks.named("assembleXCFramework"),
//            project.tasks.named("packageDistribution")
//        )

        val dir = File(localFolderPath.get())
        if (!dir.exists()) dir.mkdirs()
    }
}
