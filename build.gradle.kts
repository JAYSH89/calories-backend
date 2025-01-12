import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
  alias(libs.plugins.spotless)
  alias(libs.plugins.flyway)
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
  implementation(libs.bundles.flyway)
  implementation(libs.jackson)
  implementation(libs.kotlin.reflect)
  implementation(libs.spring.doc)
  runtimeOnly(libs.h2)
  runtimeOnly(libs.postgresql)
  testImplementation(libs.spring.boot.starter.test) {
    exclude(module = "mockito-core")
    exclude(module = "mockito-junit-jupiter")
  }
  testImplementation(libs.mockk)
  testImplementation(libs.spring.mockk)
  testImplementation(libs.kotlin.test.junit5)
  testImplementation(libs.assertk)
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

flyway {
  url = "jdbc:postgresql://localhost:5432/postgres"
  user = "postgres"
  password = "changeme!"
  driver = "postgres"
}

tasks.register<JavaExec>("generateMigrationScript") {
  group = "application"
  description = "Generate migration script in the path exposed-migration/migrations"
  classpath = sourceSets.main.get().runtimeClasspath
  mainClass = "GenerateMigrationScriptKt"
  args = listOf()
}
