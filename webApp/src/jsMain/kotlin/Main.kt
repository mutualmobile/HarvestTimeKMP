import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies
import kotlinx.browser.document
import kotlinx.browser.window
import react.create
import react.dom.client.createRoot
import kotlinext.js.require
import kotlinx.js.jso
import kotlin.js.json

val sharedComponent = SharedComponent()

fun main() {
    firebaseInit()
    initSqlDelightExperimentalDependencies()
    window.onload = { _ ->
        createRoot(document.createElement("root").also { document.body!!.appendChild(it) }).render(
            HarvestApp.create()
        )
    }
}

private fun firebaseInit() {
    val firebase: dynamic = require("firebase/app").default
    val firebaseConfig = json(
        "apiKey" to "AIzaSyAEKlvIkYeG9rcZ1FuA7KOJoiLn1o4t1YU",
        "authDomain" to "harvestkmp.firebaseapp.com",
        "projectId" to "harvestkmp",
        "storageBucket" to "harvestkmp.appspot.com",
        "messagingSenderId" to "877837074584",
        "appId" to "1:877837074584:web:0d32488e576994a668d0ed",
        "measurementId" to "G-T2KD5RPQXJ"
    )
    firebase.initializeApp(firebaseConfig)
}


suspend fun setupDriver() {
    sharedComponent.provideGithubTrendingLocal().driver?.let {} ?: run {
        setupDriverInternal()
    }

}

private suspend fun setupDriverInternal() {
    try {
        val driver = DriverFactory().createDriverBlocking()
        val trendingLocal = sharedComponent.provideGithubTrendingLocal()
        trendingLocal.driver = driver
    } catch (ex: Exception) {
        console.log(ex.message)
        console.log("Exception happened here.")
        console.log(ex)
    }
}
