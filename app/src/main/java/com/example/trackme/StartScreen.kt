package com.example.trackme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackme.UiElements.CircularProgressBar


@Composable
fun start(navController: NavController){
    Box(modifier = Modifier, contentAlignment = Alignment.Center){
        CircularProgressBar(angle = 0f)
        Button(
            onClick = { navController.navigate("session") },
            modifier = Modifier.size(220.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color.White
            )
        ) {
            Text(
                text = """Start""".trimMargin(),
                fontSize = 40.sp,
            )
        }
    }
}
