group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}
