import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// 自定义异常，表示内部出错了
class InnerException(message: String = "inner value error") : Exception(message)

/**
 * 串行操作 一对一
 */
@OptIn(ExperimentalContracts::class)
public inline fun <V, E> Result<V>.andThen(transform: (V) -> Result<E>): Result<E> {
    // kotlin 约定，告诉编译器 transform 最多执行一次
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess) {
        val value = getOrNull() ?: return Result.failure(InnerException())
        return transform(value)
    } else {
        val exception = exceptionOrNull() ?: return Result.failure(InnerException())
        return Result.failure(exception)
    }
}

/**
 * 并行操作 多对一 TODO 注意这个不是 Result的拓展方法
 */
@OptIn(ExperimentalContracts::class)
public inline fun <V> zip(block: () -> V): Result<V> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return runCatching {
        block()
    }
}

/**
 * 分发操作 一对多
 */
@OptIn(ExperimentalContracts::class)
public inline fun <V, E> Result<V>.dispatch(transform: (V) -> E): Result<E> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess) {
        val value = getOrNull() ?: return Result.failure(InnerException())
        return kotlin.runCatching {
            transform(value)
        }
    } else {
        val exception = exceptionOrNull() ?: return Result.failure(InnerException())
        return Result.failure(exception)
    }
}