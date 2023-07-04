group = rootProject.group
version = rootProject.version

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
