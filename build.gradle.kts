import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
    jacoco
}

group = "nl.jaysh"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.bundles.exposed)
    implementation(libs.jackson)
    implementation(libs.kotlin.reflect)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
        exclude(module = "mockito-junit-jupiter")
    }
    testImplementation(libs.mockk)
    testImplementation(libs.spring.mockk)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_23
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

spotless {
    val buildDirectory = layout.buildDirectory.asFileTree
    kotlin {
        target("**/*.kt")
        targetExclude(buildDirectory)
        ktlint("1.5.0").editorConfigOverride(
            mapOf(
                "indent_size" to "2",
                "continuation_indent_size" to "2",
            )
        )
        trimTrailingWhitespace()
        endWithNewline()
    }
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JacocoReport>() {
    dependsOn("test")

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(true)
    }
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}

tasks.named("build") {
    dependsOn("spotlessApply")
}
