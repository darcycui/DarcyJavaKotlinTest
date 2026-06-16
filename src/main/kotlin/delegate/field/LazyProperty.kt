package delegate.field
import exts.logD

class LazyProperty(init: () -> Int) {
    init {
        // 初始化代码块 类初始化时机
        logD(message = "init LazyProperty")
    }
    // 属性委托/代理 延迟到访问该属性时 只初始化一次
    val lazyValue: Int by lazy(init)

    // 属性委托/代理 延迟到访问该属性时 只初始化一次
    val lazyValue2: Int by lazy {
        logD(message = "init lazyValue2 by lazy+++")
        100
    }
}