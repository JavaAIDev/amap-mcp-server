plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

application {
    mainClass.set("com.javaaidev.mcp.amap.ServerKt")
}


group = "com.javaaidev"
version = "0.1.0"

val mcpVersion = "0.5.0"
val slf4jVersion = "2.0.9"
val ktorVersion = "3.1.1"
val schemaKeneratorVersion = "2.1.4"

dependencies {
    implementation("io.modelcontextprotocol:kotlin-sdk:$mcpVersion")
    implementation("org.slf4j:slf4j-nop:$slf4jVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.github.smiley4:schema-kenerator-core:$schemaKeneratorVersion")
    implementation("io.github.smiley4:schema-kenerator-serialization:$schemaKeneratorVersion")
    implementation("io.github.smiley4:schema-kenerator-jsonschema:$schemaKeneratorVersion")
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}