import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mutualmobile.harvestKmp.di.initSharedDependencies
import com.mutualmobile.harvestKmp.ui.MainView

fun main() = application {
  initSharedDependencies()
  Window(onCloseRequest = ::exitApplication) {
    MainView()
  }
}
