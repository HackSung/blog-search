import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    val kotlinVersion = "1.6.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "idea")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "blog.search"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    tasks.bootJar { enabled = false }
    tasks.jar { enabled = true }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    extra["springCloudVersion"] = "2021.0.4"
    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }
}

project(":application:blog-search-api") {
    tasks.bootJar { enabled = true }

    dependencies {
        implementation(project(":datastore:blog-data-jpa"))
        implementation(project(":shared:blog-common-module"))

        implementation("org.springframework.boot:spring-boot-starter-cache")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.yaml:snakeyaml:1.29")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        /* test */
        // junit
        testImplementation("org.junit.jupiter:junit-jupiter")

        // spring
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }

        // kotest
        testImplementation("io.kotest:kotest-assertions-core:5.3.2")
        testImplementation("io.kotest:kotest-runner-junit5:5.3.2")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.1")

        // mockk
        testImplementation("io.mockk:mockk:1.12.8")
        testImplementation("com.ninja-squad:springmockk:3.1.1")
    }
}

project(":foundation:blog-boot-config") {
    dependencies {
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        api("com.fasterxml.jackson.module:jackson-module-kotlin")

        api("org.springframework.boot:spring-boot-starter")
        api("org.springframework.boot:spring-boot-starter-validation")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    }
}

project(":foundation:blog-boot-web-config") {
    dependencies {
        api(project(":foundation:blog-boot-config"))
        api("org.springframework.boot:spring-boot-starter-web")

        testApi(project(":foundation:blog-boot-config"))
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.boot:spring-boot-starter-web")
    }
}

project(":shared:blog-common-domain") {
    dependencies {
        api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    }
}

project(":shared:blog-common-module") {
    dependencies {
        implementation(project(":foundation:blog-boot-web-config"))
        implementation(project(":datastore:blog-data-jpa"))

        api("org.springframework.cloud:spring-cloud-starter-openfeign")
    }
}

project(":datastore:blog-data-jpa") {
    dependencies {
        api(project(":foundation:blog-boot-web-config"))
        api(project(":shared:blog-common-domain"))

        api("org.springframework.boot:spring-boot-starter-data-jpa")

        runtimeOnly("com.h2database:h2")
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}




