package by.alexandr7035.votekt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import by.alexandr7035.votekt.ui.core.AppContainer
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

// AppCompatActivity extends FragmentActivity which extends ComponentActivity
// Need FragmentActivity at least for biometrics
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            VoteKtTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppContainer(navController = navController)
                }
            }
        }
    }
}
