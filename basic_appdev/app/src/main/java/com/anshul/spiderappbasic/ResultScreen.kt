package com.anshul.spiderappbasic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale


@Composable
fun ResultScreen(
    userNumber: Int,
    botNumber: Int,
    targetNumber: Float,
    winner: String,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Round Summary", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "User Number: $userNumber", fontSize = 18.sp)
                Text(text = "Bot Number: $botNumber", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Spider Number (Target):", fontSize = 14.sp)
                Text(
                    text = String.format(Locale.US, "%.2f", targetNumber),
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "WINNER:", fontSize = 16.sp)
        Text(
            text = winner,
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                onRestart()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "PLAY AGAIN")
        }
    }
}