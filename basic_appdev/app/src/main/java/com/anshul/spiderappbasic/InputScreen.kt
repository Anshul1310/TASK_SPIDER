package com.anshul.spiderappbasic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun InputScreen(onGameFinished: (Int, Int, Float, String) -> Unit) {
    var userInputValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Spider Basic - APP",
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Choose a number between 0 and 100")
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = userInputValue,
            onValueChange = { newValue: String ->
                userInputValue = newValue
            },
            label = {
                Text(text = "Your Number")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (errorMessage.isEmpty() == false) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val parsedNumber = userInputValue.toIntOrNull()

                if (parsedNumber == null) {
                    errorMessage = "Please enter a valid whole number"
                } else {
                    if (parsedNumber < 0) {
                        errorMessage = "Number cannot be less than 0"
                    } else if (parsedNumber > 100) {
                        errorMessage = "Number cannot be more than 100"
                    } else {
                        // All inputs are valid
                        errorMessage = ""

                        // Bot logic: generate random number (0-100)
                        val botNumber = Random.nextInt(0, 101)

                        // Round Execution
                        // 1. Calculate Average of 2 players
                        val averageValue = (parsedNumber + botNumber) / 2.0f

                        val spiderTarget = averageValue * 0.8f

                        val userDistance = abs(parsedNumber - spiderTarget)
                        val botDistance = abs(botNumber - spiderTarget)

                        val winnerDisplayName: String
                        if (userDistance < botDistance) {
                            winnerDisplayName = "Human"
                        } else if (botDistance < userDistance) {
                            winnerDisplayName = "Bot"
                        } else {
                            winnerDisplayName = "It's a Tie!"
                        }

                        onGameFinished(parsedNumber, botNumber, spiderTarget, winnerDisplayName)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Play", fontSize = 18.sp)
        }
    }
}