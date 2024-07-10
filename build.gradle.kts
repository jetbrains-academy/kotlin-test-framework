import java.util.Properties

plugins {
    kotlin("jvm") version "1.8.20"
    java
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    `maven-publish`
}

group = "org.jetbrains.academy.test.system"
version = "2.1.3"

allprojects {
    apply {
        plugin("kotlin")
        plugin("java")
        plugin("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        val junitJupiterVersion = "5.9.0"
        implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-console:1.9.2")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(11)
    }

    apply<io.gitlab.arturbosch.detekt.DetektPlugin>()

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config = rootProject.files("detekt.yml")
        buildUponDefaultConfig = true
        debug = true
    }

    val detektReportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
        output.set(rootProject.buildDir.resolve("reports/detekt/merge.sarif"))
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
        finalizedBy(detektReportMerge)
        reports.sarif.required.set(true)
        detektReportMerge.get().input.from(sarifReportFile)
    }
}

fun getLocalProperty(key: String, file: String = "local.properties"): String? {
    val properties = Properties()

    File("local.properties")
        .takeIf { it.isFile }
        ?.let { properties.load(it.inputStream()) }
        ?: println("File $file with properties not found")

    return properties.getProperty(key, null)
}

val spaceUsername = getLocalProperty("spaceUsername")
val spacePassword = getLocalProperty("spacePassword")

configure(subprojects) {
    val subprojectName = this.name

    // We don't need to publish the root ij project
    if (subprojectName == "ij") {
        return@configure
    }

    apply(plugin = "maven-publish")
    publishing {
        publications {
            register<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = subprojectName
                version = rootProject.version.toString()
                from(components["java"])
            }
        }

        repositories {
            maven {
                url = uri("https://packages.jetbrains.team/maven/p/kotlin-test-framework/kotlin-test-framework")
                credentials {
                    username = spaceUsername
                    password = spacePassword
                }
            }
        }
    }
}
