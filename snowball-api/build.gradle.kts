plugins {
    id("snowball.kotlin-conventions")
}

dependencies {
    api(libs.adventure.api)
    api(libs.adventure.text.serializer.plain)
    api(libs.log4j)
}

kotlin {
    explicitApi()
}
