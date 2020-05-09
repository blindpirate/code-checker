import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("org.springframework.boot") version "2.2.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("plugin.spring") version "1.3.72"
    `java-library`
}

repositories {
    mavenCentral()
    jcenter()
}

java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    val junit5Version = "5.6.2"
    val jacksonVersion = "2.11.0"
    val checkstyleVersion = "8.32"

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.puppycrawl.tools:checkstyle:$checkstyleVersion")
    implementation("io.github.java-diff-utils:java-diff-utils:4.5")
    implementation("com.github.stkent:githubdiffparser:1.0.1")



    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5Version")

}

tasks.named("test", Test::class.java) {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
