package org.example.copy

fun main() {
    val testCopy = TestCopy()
    val user = User("John", 30, School("BeijingSecondSchool"))
    println("user=${user.hashCode()} $user")
    val copiedUser = user.copy()
//    val copiedUser = testCopy.copyUser(user) {
//        // data class 自带copy方法 复制对象
//        user.copy(name = "John2")
//    }
    println("copiedUser=${copiedUser.hashCode()} $copiedUser")
    copiedUser.age = 25
    println("copiedUser=${copiedUser.hashCode()} $copiedUser")
    println("user=${user.hashCode()} $user")

}

class TestCopy {
    fun copyUser(user: User, copy2: User.() -> User): User {
        return user.copy2()
    }
}

data class User(var name: String, var age: Int, var school: School)
data class School(var name: String)