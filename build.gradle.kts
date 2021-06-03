import com.google.protobuf.gradle.*
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    id("org.jetbrains.kotlin.kapt") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "1.5.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.32"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.4.32"
    id("com.google.protobuf") version "0.8.15"
}

version = "0.1"
group = "br.com.zup.ggwadera"

val kotlinVersion= project.properties["kotlinVersion"]

val protocVersion = "3.14.0"
val grpcVersion = "1.33.1"
val grpcKotlinVersion = "1.1.0"

repositories {
    mavenCentral()
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("br.com.zup.ggwadera.*")
    }
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
    implementation("org.hibernate:hibernate-validator:6.1.6.Final")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.xml:micronaut-jackson-xml")

    annotationProcessor("io.micronaut:micronaut-validation")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.mockito:mockito-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")

}


application {
    mainClass.set("br.com.zup.ggwadera.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }


}
sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protocVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }
}
