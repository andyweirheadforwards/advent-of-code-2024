plugins {
    kotlin("jvm") version "2.1.0"
    id("org.sonarqube") version "6.0.1.5171"
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

val kotlinxVersion = "1.9.0"
val caffeineVersion = "3.0.5"
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }

    test {
        kotlin {
            srcDirs("test")
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "andyweirheadforwards_advent-of-code-2024")
        property("sonar.organization", "andyweirheadforwards")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.language", "kotlin")
        property("sonar.sources", "src")
        property("sonar.tests", "test")
        property("sonar.qualitygate.wait", "true")
    }
}
