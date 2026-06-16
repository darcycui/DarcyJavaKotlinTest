package companion

import exts.logD

fun TestCompanion.Companion.hello() {
    logD(message = "伴生对象的拓展方法: hello")
}

fun TestCompanion.log() {
    logD(message = "拓展方法: log")
}

/**
 * 拓展属性 不能直接赋值 TODO 通过get()获取值
 */
var TestCompanion.id: String
    get() = "id-100"
    set(value) {
        // 拓展属性没有 back field 不能赋值
    }