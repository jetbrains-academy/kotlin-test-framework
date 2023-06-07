import java.util.Properties

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    `maven-publish`
}

group = "org.jetbrains.academy.test.system"
version = "1.0.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    val junitJupiterVersion = "5.9.0"
    implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.9.2")
}

fun getLocalProperty(key: String, file: String = "local.properties"): String? {
    val properties = Properties()

    File("local.properties")
        .takeIf { it.isFile }
        ?.let { properties.load(it.inputStream()) }
        ?: println("File $file with properties not found")

    return properties.getProperty(key, null)
}

tasks.test {
    useJUnitPlatform()
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

val spaceUsername = getLocalProperty("spaceUsername")
val spacePassword = getLocalProperty("spacePassword")

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
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
