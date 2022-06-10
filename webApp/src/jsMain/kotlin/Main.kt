import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.AuthApiUseCaseComponent
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies
import com.mutualmobile.harvestKmp.domain.model.request.DevicePlatform
import com.mutualmobile.harvestKmp.domain.model.request.User
import firebase.app.App
import firebase.messaging.messaging
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.create
import react.dom.client.createRoot
import kotlin.js.json

var firebaseApp: App? = null

val webKey =
    "BFmePGx52AaCaDZzf-0qq8-oF9VT6fATcprqeY4SBWxnJO7BKp1Snsixnt_M0pecIVaPpBN3I1vhPHZbFIu0w5Y" // TODO move this

val sharedComponent = SharedComponent()
val mainScope = MainScope()

suspend fun main() {
    firebaseInit()
    initSqlDelightExperimentalDependencies()
    mainScope.launch {
        setupDriver()
    }
    window.onload = { _ ->
        createRoot(document.createElement("root").also { document.body!!.appendChild(it) }).render(
            HarvestApp.create()
        )
    }
}

fun firebaseInit() {
    val firebaseConfig = json(
        "apiKey" to "AIzaSyAEKlvIkYeG9rcZ1FuA7KOJoiLn1o4t1YU",
        "authDomain" to "harvestkmp.firebaseapp.com",
        "projectId" to "harvestkmp",
        "storageBucket" to "harvestkmp.appspot.com",
        "messagingSenderId" to "877837074584",
        "appId" to "1:877837074584:web:0d32488e576994a668d0ed",
        "measurementId" to "G-T2KD5RPQXJ"
    )

    firebaseApp = firebase.initializeApp(firebaseConfig)
}

fun setupFcmPush() {
    firebaseApp?.messaging()?.requestPermission()?.then {
        firebaseApp?.messaging()?.getToken(webKey)?.then {
            sendTokenToServer(it)
        }?.catch {
            console.log(it)
        }
    }?.catch {
        console.log(it)
    }
}

fun sendTokenToServer(it: String?) {
    mainScope.launch {
        AuthApiUseCaseComponent().provideFcmTokenUseCase()
            .invoke(User(platform = DevicePlatform.Web, pushToken = it))
    }
}


suspend fun setupDriver() {
    sharedComponent.provideHarvestUserLocal().driver?.let {} ?: run {
        setupDriverInternal()
    }
}

private suspend fun setupDriverInternal() {
    try {
        val driver = DriverFactory().createDriverBlocking()
        val harvestUser = sharedComponent.provideHarvestUserLocal()
        harvestUser.driver = driver
    } catch (ex: Exception) {
        console.log(ex.message)
        console.log("Exception happened here.")
        console.log(ex)
    }
}
