import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.io.ByteArrayOutputStream


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    //id("io.github.donadev.kmm.ios_deploy.plugin") version "0.0.20"
}

version = "1.0.21"
val iOSBinaryName = "shared"
val IOS_PUBLISHING = "ios_publishing"

//val aaPodspecTask by tasks.registering(APodspecTask::class)
//val aaPodspecDeployTask by tasks.registering(PodspecDeployTask::class)
//
//aaPodspecDeployTask.configure {
//    podspecName.convention("sharedLibraryZhurid.podspec")
//}
//
//
//aaPodspecTask.configure {
//    val extensionName = "iosDeploy"
//    val extensionsa = project.extensions.create(extensionName, DeployExtension::class.java)
//    extensionsa.summary = "summary"
//    extensionsa.homepage = "homepage"
//    extensionsa.gitUrl = ""
//    extensionsa.authors = ""
//    extensionsa.licenseType = ""
//    extensionsa.licenseFile = ""
//    extensionsa.specRepository = SpecRepository(
//        name = "name",
//        url = "url",
//    )
//    extension.convention(
//        extensionsa
//    )
//    xcFrameworkPath.convention("shared.xcframework")
//}

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
}

val gitCommit by tasks.registering(Exec::class) {
    group = IOS_PUBLISHING
    dependsOn("assembleXCFramework", "packageDistribution")

    onlyIf { gitStatus.get().standardOutput.toString().trim().isNotEmpty() }
    doLast {
        commandLine("git", "commit", "-am", "Автоматический коммит")
    }
}

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



abstract class APodspecTask : DefaultTask() {

    init {
        group = IOS_PUBLISHING
        description = "Generate podspec"
    }

    @get:Input
    abstract val extension: Property<DeployExtension>

    @get:Input
    abstract val xcFrameworkPath: Property<String>

    private fun getPodspec(extension: DeployExtension) : String {
        return """
            Pod::Spec.new do |spec|
                spec.name                     = '${project.name}'
                spec.version                  = '${project.version}'
                spec.homepage                 = '${extension.homepage.get()}'
                spec.source                   = { :git => "${extension.gitUrl.get()}", :tag => spec.version.to_s }
                spec.authors                  = '${extension.authors.get()}'
                spec.license                  = { :type => '${extension.licenseType.get()}', :file => '${extension.licenseFile.get()}' }
                spec.summary                  = '${extension.summary.get()}'
                spec.vendored_frameworks      = "${xcFrameworkPath.get()}"
                spec.libraries                = "c++"
                spec.static_framework         = true
                spec.module_name              = "#{spec.name}_umbrella"
                spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
                spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
                spec.ios.deployment_target = '11.0'
            end
        """.trimIndent()
    }

    @TaskAction
    fun execute() {
        val podspec = getPodspec(extension.get())
        val outFile = File(project.rootDir, "${project.name}.podspec")
        outFile.writeText(podspec)
    }
}

abstract class DeployExtension @Inject constructor() {

    abstract val summary: Property<String>
    abstract val specRepository: Property<SpecRepository>
    abstract val gitUrl: Property<String>
    abstract val homepage: Property<String>
    abstract val authors: Property<String>
    abstract val licenseType: Property<String>
    abstract val licenseFile: Property<String>

}

data class SpecRepository(val name : String, val url : String)

abstract class PodspecDeployTask: Exec() {

    init {
        description = "Deploys podspec"
        //dependsOn("aaPodspecTask")
        //dependsOn("podRepo")
        workingDir = project.rootDir
        group = IOS_PUBLISHING
    }

    @get:Input
    abstract val podspecName: Property<String>

    override fun exec() {
        executable = "pod"
        args(mutableListOf<String>().apply {
            add("trunk")
            add("push")
            add(podspecName.get())
            add("--allow-warnings")
            //add(extension.get().specRepository.get().name)
        })
        super.exec()
    }
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

val getCurrentPublishedPodVersion by tasks.registering{
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
        if (version != null) {
            println("Current published version of $podName is: $version")
        } else {
            println("Unable to find the current published version of $podName.")
        }
    }
}