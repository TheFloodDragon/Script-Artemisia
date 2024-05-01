plugins {
    application
}

dependencies {
    shadowModule("module-gson")
    shadowModule("module-common")
    shadowModule("module-compiler")
    shadowModule("module-vm")
}

application {
    mainClass.set("${group}.application.Artemisia")
}