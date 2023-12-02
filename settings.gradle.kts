pluginManagement {
    repositories {
        repositories {
            maven {
                name = "Huawei Maven"
                setUrl("https://repo.huaweicloud.com/repository/maven/")
            }
            maven {
                name = "spigotmc-repo"
                setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
            }
            maven {
                setUrl("https://maven.google.com")
            }
            google()
            maven { setUrl("https://jitpack.io") }
            mavenCentral()

        }
    }
}

rootProject.name = "MScript"