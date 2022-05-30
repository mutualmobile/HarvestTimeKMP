@file:JsModule("firebase/app")
@file:JsNonModule
@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "EXTERNAL_DELEGATION"
)

package firebase

import firebase.app.App

external interface FirebaseError {
    var code: String
    var message: String
    var name: String
    var stack: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Observer<T, E> {
    var next: NextFn<T>
    var error: ErrorFn<E>
    var complete: CompleteFn
}

external var SDK_VERSION: String

external fun registerVersion(library: String, version: String, variant: String? = definedExternally)

external fun app(name: String? = definedExternally): App

external var apps: Array<App>

external fun initializeApp(options: Any, name: String? = definedExternally): App