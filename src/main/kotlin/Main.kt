import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.AppServices

private val coroutineScope = CoroutineScope(Dispatchers.IO)

private val appServices by lazy { AppServices.create() }

private lateinit var database: Database

@Composable
@Preview
private fun Application() {
    var text by remember { mutableStateOf("Hello, world!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Realm!"
        }) {
            Text(text)
        }
    }
}

fun main() {
    coroutineScope.launch {
        database = if (appServices.isUserLoggedIn) {
            Database.create(appServices.currentUser!!)
        } else {
            val user = appServices.login()
            Database.create(user)
        }

        // Create 4 products for test.
        // database.addProduct1()
        // database.addProduct2()
        // database.addProduct3()
        // database.addProduct4()

        // Collect results...
        database.findProducts().collect {
            it.forEach { product ->
                println("Returned ${product.description} - ${product.brand} (${product._id.toHexString()})")
            }
        }
    }

    application {
        Window(onCloseRequest = ::exitApplication) {
            Application()
        }
    }

    database.close()
}
