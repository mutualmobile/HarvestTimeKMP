import com.mutualmobile.harvestKmp.db.DriverFactory
import com.mutualmobile.harvestKmp.di.SharedComponent
import com.mutualmobile.harvestKmp.di.initSqlDelightExperimentalDependencies
import kotlinx.browser.document
import kotlinx.browser.window
import react.create
import react.dom.client.createRoot

val sharedComponent = SharedComponent()

fun main() {
    initSqlDelightExperimentalDependencies()
    window.onload = { _ ->
        createRoot(document.getElementById("root")!!).render(HarvestApp.create())
    }
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
