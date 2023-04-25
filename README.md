[![official project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Gradle Build](https://github.com/jetbrains-academy/kotlin-test-framework/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/jetbrains-academy/kotlin-test-framework/actions/workflows/gradle-build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# Kotlin test framework

This repository contains a framework for creating tests in Kotlin. 
Currently, this framework is used in the Kotlin Onboarding courses (see the [Marketplace](https://plugins.jetbrains.com/education)) 
to test student solutions. It contains functionality to test student solutions 
by using the [Java Reflection API](https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/index.html) under the hood. 
The proposed wrappers allow us to check the necessary Kotlin properties, 
such as `val` and `var` modifiers or whether the class is a data class for any Java objects.

You can find several usage examples in the [test](./src/test/kotlin/org/jetbrains/academy/test/system) folder.

## Getting started

The project uses Java 11.

### Include as a library

Add the following dependency into the `Gradle.kts` file:

```kotlin
repositories {
   maven("https://packages.jetbrains.team/maven/p/kotlin-test-framework/kotlin-test-framework")
}

dependencies {
    implementation("org.jetbrains.academy.test.system:kotlin-test-system:$latest_version")
}
```

### Build from sources

1. Clone this repository:
```text
git clone https://github.com/jetbrains-academy/kotlin-test-framework.git
```

2. Build the project:
```text
./gradlew build
```

### Run tests

To run tests locally, you just need to build the project and run the following command:

```text
./gradlew test
```

## Want to know more?

If you have questions about the framework or if you find some errors,
you can ask questions and participate in discussions in repository [issues](https://github.com/jetbrains-academy/kotlin-test-framework/issues).

## Contribution

Please be sure to review the [project's contributing guidelines](./contributing.md) to learn how to help the project.
