description = "core"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
}
subprojects{
    dependencies{
        implementation(project(":project"))
    }

}