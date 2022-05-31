import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    // groovy
    kotlin("jvm") version "1.6.21"
    checkstyle
    distribution
    maven
    id("org.omegat.gradle") version "1.5.7"
}

version = "0.0.1"

omegat {
    version = "5.7.1"
    pluginClass = "divvun.Divvun"
}

dependencies {
    packIntoJar("org.slf4j:slf4j-api:1.7.21")
    // DivvunSpell FFI components
    packIntoJar("com.github.divvun:divvunspell-sdk-java:df49bed15b")

    packIntoJar("net.java.dev.jna:jna:5.11.0")
//    implementation("commons-io:commons-io:2.5")
//    implementation("commons-lang:commons-lang:2.6")
    implementation("org.slf4j:slf4j-nop:1.7.21")
    testImplementation("junit:junit:4.12")
    testImplementation("xmlunit:xmlunit:1.6")
    testImplementation("org.madlonkay.supertmxmerge:supertmxmerge:2.0.1")
    testImplementation("com.alibaba:fastjson:1.2.17")
    packIntoJar(kotlin("stdlib-jdk8"))
}

checkstyle {
    isIgnoreFailures = true
    toolVersion = "7.1"
}

distributions {
    main {
        contents {
            from(tasks["jar"], "README.md", "COPYING", "CHANGELOG.md")
        }
    }
}
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}