plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("kapt") version "1.4.21"
    kotlin("plugin.allopen") version "1.4.21"
    kotlin("plugin.jpa") version "1.4.21"

    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.2.0"
}

version = "1.0.0"
group = "com.nanabell.discord"

repositories {
    mavenCentral()
    jcenter()
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.nanabell.discord.tsp.*")
    }
}

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
        arg("micronaut.processing.annotations", "com.nanabell.discord.tsp.*")
        arg("micronaut.processing.group", "com.nanabell.discord")
        arg("micronaut.processing.module", "the-scratching-post")
    }
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")

    implementation(kotlin("stdlib"))
    // implementation(kotlin("reflect"))

    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-http-server")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")

    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")

    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")

    implementation("net.dv8tion:JDA:4.2.0_225") { exclude("opus-java", "opus-java-natives") }
    implementation("com.jagrosh:jda-utilities:3.0.5")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testRuntimeOnly("org.testcontainers:postgresql")
}


application {
    mainClass.set("com.nanabell.discord.tsp.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("14")
}

tasks {

    compileKotlin {
        kotlinOptions {
            jvmTarget = "14"
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "14"
            javaParameters = true
        }
    }

    shadowJar {
        mergeServiceFiles()
    }
}
