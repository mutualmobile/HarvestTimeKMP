import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mutualmobile.harvestKmp.di.initSharedDependencies

@Composable
@Preview
fun App() {
    MaterialTheme {
      Column {
        Text(
          text = "Hello KMM Desktop!",
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        )
      }
    }
}

fun main() = application {
    initSharedDependencies()
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
