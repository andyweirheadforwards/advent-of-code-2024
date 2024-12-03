plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
sourceSets {
    main {
        kotlin {
            srcDirs("src")
        }
    }

    test {
        kotlin {
            srcDirs("test")
        }
    }
}