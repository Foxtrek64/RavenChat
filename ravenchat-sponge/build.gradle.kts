import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow")
    id("org.spongepowered.gradle.plugin") version "2.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ravenchat-common"))
}

sponge {
    apiVersion("8.0.0")
    license("Internet Systems Consortium (ISC) License")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("ravenchat") {
        displayName("Ravenchat")
        entrypoint("com.luzfaltex.ravenchat.RavenChatSponge")
        description("A feature-parity remake of VentureChat by Aust1n46 for Sponge, Fabric, and Paper.")
        links {
            homepage("https://github.com/ravenrockrp/RavenChat")
            // source("https://spongepowered.org/source")
            // issues("https://spongepowered.org/issues")
        }
        contributor("LuzFaltex_Contributors") {
            description("Author")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

val javaTarget = 8 // Sponge targets a minimum of Java 8
java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
    if (JavaVersion.current() < JavaVersion.toVersion(javaTarget)) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaTarget))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.apply {
        encoding = "utf-8" // Consistent source file encoding
        if (JavaVersion.current().isJava10Compatible) {
            release.set(javaTarget)
        }
    }
}

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks {
    shadowJar {
        dependencies {
            include(dependency(":ravenchat-common"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
