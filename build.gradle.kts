plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    val coroutineVersion = "1.9.0"
    testImplementation("org.jetbrains.kotlin:kotlin-test:$coroutineVersion")
    // 核心协程库
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    // 三方加密库 包含SM加密算法实现 Scrypt算法
    // https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on
    implementation ("org.bouncycastle:bcprov-jdk18on:1.79")

    // Bcrypt算法
    implementation ("org.mindrot:jbcrypt:0.4")
    // Argon2算法
    implementation ("de.mkammerer:argon2-jvm:2.11")

}

//tasks.test {
//    useJUnitPlatform()
//}
//kotlin {
//    jvmToolchain(17)
//}