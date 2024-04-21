plugins {
    application
}

dependencies {
    shadowModule("module-gson")
    shadowModule("module-common")
    shadowModule("module-compiler")
}

application {
    mainClass.set("${group}.application.Artemisia")
}