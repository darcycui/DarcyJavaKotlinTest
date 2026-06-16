package copy
import exts.logD

fun main() {
    val user = User("John", 30, School("BeijingSecondSchool"))
    logD(message = "user=${System.identityHashCode(user)} school=${System.identityHashCode(user.school)} $user")
    // data class 自带copy方法 复制对象
    val copiedUser = user.copy(
        // 嵌套对象 手动复制
        school = user.school.copy()
    )
    logD(message = "copiedUser=${System.identityHashCode(copiedUser)} school=${System.identityHashCode(copiedUser.school)} $copiedUser")
    copiedUser.age = 25
    logD(message = "copiedUser=${System.identityHashCode(copiedUser)} school=${System.identityHashCode(copiedUser.school)} $copiedUser")
    logD(message = "user=${System.identityHashCode(user)} school=${System.identityHashCode(user.school)} $user")

}

class TestCopy {
    fun copyUser(user: User, copy2: User.() -> User): User {
        return user.copy2()
    }
}

data class User(var name: String, var age: Int, var school: School)
data class School(var name: String)