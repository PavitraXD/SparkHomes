plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.pavitraxd"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.shadowJar {
    archiveFileName.set("SparkHomes-${version}.jar")
    relocate("org.bukkit", "com.pavitraxd.sparkhomes.libs.bukkit")
    relocate("org.bukkit.command", "com.pavitraxd.sparkhomes.libs.bukkit.command")
    relocate("org.bukkit.configuration", "com.pavitraxd.sparkhomes.libs.bukkit.configuration")
}

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()
}
