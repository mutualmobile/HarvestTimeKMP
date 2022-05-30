@file:JsModule("firebase/messaging")
@file:JsNonModule
@file:JsQualifier("messaging")
@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "EXTERNAL_DELEGATION"
)

package firebase.messaging

import firebase.*
import org.w3c.workers.ServiceWorkerRegistration
import kotlin.js.Promise

external interface Messaging {
    fun deleteToken(token: String): Promise<Boolean>
    fun getToken(vapidKey:String?): Promise<String>
    fun <E> onMessage(
        nextOrObserver: NextFn<Any>,
        error: ErrorFn<E>? = definedExternally,
        completed: CompleteFn? = definedExternally
    ): Unsubscribe

    fun <E> onMessage(
        nextOrObserver: Observer<Any, Error>,
        error: ErrorFn<E>? = definedExternally,
        completed: CompleteFn? = definedExternally
    ): Unsubscribe

    fun <E> onTokenRefresh(
        nextOrObserver: NextFn<Any>,
        error: ErrorFn<E>? = definedExternally,
        completed: CompleteFn? = definedExternally
    ): Unsubscribe

    fun <E> onTokenRefresh(
        nextOrObserver: Observer<Any, Error>,
        error: ErrorFn<E>? = definedExternally,
        completed: CompleteFn? = definedExternally
    ): Unsubscribe

    fun requestPermission(): Promise<Unit>
    fun setBackgroundMessageHandler(callback: (payload: Any) -> dynamic)
    fun useServiceWorker(registration: ServiceWorkerRegistration)
    fun usePublicVapidKey(b64PublicKey: String)
}

external fun isSupported(): Boolean
