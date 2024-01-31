package de.hsworms.android.modernwalkingapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import de.hsworms.android.modernwalkingapp.presentation.theme.ModernWalkingAppTheme
import de.hsworms.android.modernwalkingapp.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModernWalkingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    MainScreen()

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ModernWalkingAppTheme {
//        MyApp()
//    }
//}