@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "EXTERNAL_DELEGATION"
)

package firebase

typealias NextFn<T> = (value: T) -> Unit

typealias ErrorFn<E> = (error: E) -> Unit

typealias CompleteFn = () -> Unit

typealias Unsubscribe = () -> Unit