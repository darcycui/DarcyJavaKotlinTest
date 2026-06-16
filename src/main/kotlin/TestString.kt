
import exts.logD
fun main() {
    val sql = "PRAGMA cipher_kdf_algorithm = 'PBKDF2_HMAC_SHA512';"
    logD(message = "sql=$sql")
    val sql2 = "PRAGMA cipher_kdf_algorithm = \'PBKDF2_HMAC_SHA512\';"
    logD(message = "sql2=$sql2")
}