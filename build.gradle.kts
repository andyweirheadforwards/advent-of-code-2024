plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
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