import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.huaweicloud.com/repository/maven/")
    maven("https://jitpack.io")
}


dependencies {

}
tasks {
    test {
        useJUnitPlatform()
    }
    withType<JavaCompile> { options.encoding = "UTF-8" }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
publishing {
    publications {
        register<MavenPublication>("publishToMavenLocal") {
            from(components["java"])
        }
    }
}
