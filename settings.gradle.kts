pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    
}

include(
    "core",
    "ij",
)

rootProject.name = "kotlin-test-system"
include("ij:kotlin-psi")
findProject(":ij:kotlin-psi")?.name = "kotlin-psi"
include("ij:java-psi")
findProject(":ij:java-psi")?.name = "java-psi"
include("ij:core")
findProject(":ij:core")?.name = "core"
