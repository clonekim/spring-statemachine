
plugins {
    java
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.19.18"
}

group = "ssm-server"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java", "src/build/java")
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val versions = mapOf(
    "statemachine" to "4.0.0"
)

dependencyManagement {
    imports {
        mavenBom("org.springframework.statemachine:spring-statemachine-bom:${versions["statemachine"]}")
    }
}

dependencies {
    implementation("org.springframework.statemachine:spring-statemachine-starter")
    implementation("org.springframework.statemachine:spring-statemachine-recipes-common")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework:spring-webflux")
    compileOnly("org.jetbrains:annotations:26.0.2")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.statemachine:spring-statemachine-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    jooqCodegen("org.postgresql:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName.set("uber.jar")
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/example?options=-c%20timezone=Asia/Seoul"
            user = "example"
            password = "secret"
        }

        generator {
            name = "org.jooq.codegen.DefaultGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "public"
                includes = ".*"
                logSlowQueriesAfterSeconds = 20
            }

            generate {
                isDeprecated = false
                isRecords = true
                isImmutablePojos = false
                isFluentSetters = true
                isDefaultCatalog = false
                isDefaultSchema = false
                isRoutines = false
            }

            target {
                packageName = "com.innogrid"
                directory = "src/build/java"
            }
            strategy {
                name = "org.jooq.codegen.DefaultGeneratorStrategy"
            }
        }


    }
}
