package copy
import exts.logD

fun main() {
    val tom = User("Tom", 30, School("BeijingSecondSchool"))
    val jerry = User("Jerry", 30, School("BeijingSecondSchool"))
    var userList = listOf(tom, jerry)
    logD(message = "userList1: ${System.identityHashCode(userList)}")
    userList.forEach {
        logD(message = "${it.name} ${System.identityHashCode(it)}")
    }
    userList = userList.map {
        if (it.name == "Tom") {
            it.copy()
        } else {
            it
        }
    }
    logD(message = "userList2: ${System.identityHashCode(userList)}")
    userList.forEach {
        logD(message = "${it.name} ${System.identityHashCode(it)}")
    }

}