import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    id("io.izzel.taboolib") version "2.0.27"
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    id("org.jetbrains.dokka") version "1.6.0"
}

taboolib {
    env {
        // 安装模块
        install(Basic)
        install(Bukkit)
        install(BukkitUtil)
        install(BukkitHook)
        install(CommandHelper)
        install(Database)
        install(MinecraftEffect)
        install(Kether)
        install(Metrics)
        install(BukkitNavigation)
        install(BukkitNMSEntityAI)
        install(BukkitNMS)
        install(BukkitNMSUtil)
        install(BukkitUI)
        install(JavaScript)
    }
    version {
        taboolib = "6.2.4-abd325ee"
        // 跳过 Kotlin 重定向
//         skipKotlinRelocate = true
        // 跳过 TabooLib 重定向
//         skipTabooLibRelocate = true
    }
    relocate("ink.ptms.um", "ink.ptms.chemdah.um")
    description {
        contributors {
            name("坏黑")
            name("PengLx")
            name("AmazingOcean")
            name("R-Josef")
            name("Galaxy-VN")
            name("Tale_sh")
        }
        dependencies {
            name("Adyeshach")
            name("HipeCurrency")
        }
    }
}

repositories {
    maven { url = uri("https://repo.pcgamingfreaks.at/repository/maven-everything") }
    maven { url = uri("https://jitpack.io") }
    mavenLocal()
    mavenCentral()
}

dependencies {
    // adyeshach
    compileOnly("ink.ptms.adyeshach:all:2.0.0-snapshot-4")
    compileOnly("mc.tale_sh.redislib:RedisLib:0.1.0")
    compileOnly("ink.ptms:error_reporter:1.0.0")
    compileOnly("net.milkbowl.vault:Vault:1")
    compileOnly("com.sk89q.worldedit:WorldEdit:7")
    compileOnly("public:CustomGo:1.0.0")
    compileOnly("public:Skript:1.0.0")
    compileOnly("public:Parties:1.0.0")
    compileOnly("ink.ptms:Zaphkiel:1.6.0")
    compileOnly("ink.ptms:Sandalphon:1.3.0")
    compileOnly("ink.ptms.core:v11904:11904:mapped")
    compileOnly("ink.ptms.core:v11400:11400")
    compileOnly("ink.ptms:nms-all:1.0.0")
    implementation(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.0")

    compileOnly("com.bh.planners:Planners:1.0.6")
    compileOnly("com.hitable.hipecurrency:HipeCurrency:0.1.0")
    compileOnly("com.hitable.hipeequip:HipeEquip:1.0.0")
    compileOnly("cn.glory.legendengine:LegendPlugin:1.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
        freeCompilerArgs.addAll(listOf("-Xjvm-default=all", "-Xextended-compiler-checks"))
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = "ink.ptms"
        }
    }
}
