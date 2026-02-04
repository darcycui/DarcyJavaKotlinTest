package org.example.copy

fun main() {
    val tom = User("Tom", 30, School("BeijingSecondSchool"))
    val jerry = User("Jerry", 30, School("BeijingSecondSchool"))
    var userList = listOf(tom, jerry)
    println("userList1: ${System.identityHashCode(userList)}")
    userList.forEach {
        println("${it.name} ${System.identityHashCode(it)}")
    }
    userList = userList.map {
        if (it.name == "Tom") {
            it.copy()
        } else {
            it
        }
    }
    println("userList2: ${System.identityHashCode(userList)}")
    userList.forEach {
        println("${it.name} ${System.identityHashCode(it)}")
    }

}