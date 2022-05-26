import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mutualmobile.harvestKmp.di.initSharedDependencies

@Composable
@Preview
fun App() {
    MaterialTheme {}
}

fun main() = application {
    initSharedDependencies()
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
