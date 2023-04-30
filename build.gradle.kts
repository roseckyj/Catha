plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.5.4"
  id("xyz.jpenilla.run-paper") version "2.0.1"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cz.xrosecky"
version = "0.1.0"
description = "Catha wrapper plugin"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://repo.unnamed.team/repository/unnamed-public/")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
  paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")

  // Creative by Unnamed Team
  implementation("team.unnamed:creative-api:0.4.1-SNAPSHOT")
  implementation("team.unnamed:creative-server:0.5.4-SNAPSHOT")

  // Hephaestus engine by Unnamed Team
  implementation("team.unnamed:hephaestus-api:0.4.1-SNAPSHOT")
  implementation("team.unnamed:hephaestus-reader-blockbench:0.4.1-SNAPSHOT")
  implementation("team.unnamed:hephaestus-runtime-bukkit-api:0.4.1-SNAPSHOT")
  implementation("team.unnamed:hephaestus-runtime-bukkit-adapt-v1_18_R2:0.4.1-SNAPSHOT:reobf")
}

tasks {
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name()
    val props = mapOf(
      "name" to project.name,
      "version" to project.version,
      "description" to project.description,
      "apiVersion" to "1.18"
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }
}
