package id.co.integrapratama.bnipayment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import id.co.integrapratama.bnipayment.navigation.AppNavHost
import id.co.integrapratama.bnipayment.ui.theme.BNIPaymentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BNIPaymentTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
