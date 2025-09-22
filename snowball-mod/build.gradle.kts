plugins {
    id("snowball.kotlin-conventions")
    id("fabric-loom") version "1.11-SNAPSHOT"
}

repositories {
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)
    modImplementation(libs.fabric.api)
    implementation(project(":snowball-api"))

    testImplementation(kotlin("test"))
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to libs.versions.minecraft.get(),
            "loader_version" to libs.versions.fabric.loader.get(),
            "kotlin_loader_version" to libs.versions.fabric.language.kotlin.get()
        )
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
