plugins {
    id("snowball.kotlin-conventions")
    id("net.fabricmc.fabric-loom") version "1.14-SNAPSHOT"
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.language.kotlin)
    implementation(libs.fabric.api)
    implementation(project(":snowball-api"))

    testImplementation(kotlin("test"))
}

tasks.processResources {
    filteringCharset = "UTF-8"

    val properties = arrayOf(
        "version" to project.version,
        "minecraft_mod_version" to libs.versions.minecraft.mod.get(),
        "loader_version" to libs.versions.fabric.loader.get(),
        "kotlin_loader_version" to libs.versions.fabric.language.kotlin.get()
    )

    inputs.properties(*properties)
    filesMatching("fabric.mod.json") {
        expand(*properties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    from("../LICENSE") {
        rename { "${it}_snowball" }
    }
}

tasks.test {
    useJUnitPlatform()
}
