plugins {
    kotlin("jvm") version "1.8.20"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
}

group = "org.jetbrains.academy.test.system"
version = "1.0"

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
