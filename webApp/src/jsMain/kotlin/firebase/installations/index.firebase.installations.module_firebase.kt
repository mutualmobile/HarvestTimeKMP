@file:JsModule("firebase")
@file:JsNonModule
@file:JsQualifier("installations")
@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "EXTERNAL_DELEGATION"
)

package firebase.installations

import kotlin.js.Promise

external interface Installations {
    fun getId(): Promise<String>
    fun getToken(forceRefresh: Boolean? = definedExternally): Promise<String>
    fun delete(): Promise<Unit>
    fun onIdChange(callback: (installationId: String) -> Unit): () -> Unit
}
