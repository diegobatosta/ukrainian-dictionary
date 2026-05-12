plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"

    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.20.0"
    id("org.springframework.boot") version "4.0.3"
}

group = "com.tosta"
version = project.property("version")!!
description = "A Ukrainian dictionary with Russian and English translations"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin {
            srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}

dependencies {
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("tools.jackson.module:jackson-module-kotlin")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation(libs.assertj)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-webtestclient")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// Explicitly attaches Mockito to test execution. Read more here:
// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
val mockitoAgent: Configuration = configurations.create("mockitoAgent")
dependencies {
    testImplementation(libs.mockito)
    mockitoAgent(libs.mockito) { isTransitive = false }
}
tasks {
    test {
        jvmArgs.add("-javaagent:${mockitoAgent.asPath}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.clean {
    delete(layout.buildDirectory.dir("generated"))
}
