package org.example

fun main() {
    val sql = "PRAGMA cipher_kdf_algorithm = 'PBKDF2_HMAC_SHA512';"
    println("sql=$sql")
    val sql2 = "PRAGMA cipher_kdf_algorithm = \'PBKDF2_HMAC_SHA512\';"
    println("sql2=$sql2")
}