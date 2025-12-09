plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val langchain4jVersion = "0.33.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("dev.langchain4j:langchain4j:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-open-ai:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-chroma:$langchain4jVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Ensure UTF-8 console output so Korean text does not break.
    systemProperty("file.encoding", "UTF-8")
    jvmArgs("--add-opens", "java.base/java.io=ALL-UNNAMED")
}
