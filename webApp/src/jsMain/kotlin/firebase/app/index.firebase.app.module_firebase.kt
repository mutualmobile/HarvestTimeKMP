@file:JsModule("firebase/app")
@file:JsNonModule
@file:JsQualifier("app")
@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "EXTERNAL_DELEGATION"
)

package firebase.app

import firebase.messaging.Messaging
import kotlin.js.Promise

external interface App {
    fun delete(): Promise<Any>
    var name: String
    var options: Any
}
