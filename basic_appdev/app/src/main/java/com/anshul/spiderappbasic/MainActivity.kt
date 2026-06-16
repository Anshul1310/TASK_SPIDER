package com.anshul.spiderappbasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anshul.spiderappbasic.ui.theme.SpiderAppBasicTheme
import kotlin.math.abs
import kotlin.random.Random
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiderAppBasicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "game_input") {
        composable(route = "game_input") {
            InputScreen(onGameFinished = { userNum: Int, botNum: Int, target: Float, winner: String ->
                navController.navigate("game_result/$userNum/$botNum/$target/$winner")
            })
        }
        composable(
            route = "game_result/{userNum}/{botNum}/{target}/{winner}",
            arguments = listOf(
                navArgument("userNum") { type = NavType.IntType },
                navArgument("botNum") { type = NavType.IntType },
                navArgument("target") { type = NavType.FloatType },
                navArgument("winner") { type = NavType.StringType }
            )
        ) { entry: NavBackStackEntry ->
            val userValue: Int = entry.arguments?.getInt("userNum") ?: 0
            val botValue: Int = entry.arguments?.getInt("botNum") ?: 0
            val targetValue: Float = entry.arguments?.getFloat("target") ?: 0f
            val winnerName: String = entry.arguments?.getString("winner") ?: ""

            ResultScreen(
                userNumber = userValue,
                botNumber = botValue,
                targetNumber = targetValue,
                winner = winnerName,
                onRestart = {
                    navController.navigate("game_input") {
                        popUpTo("game_input") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}




