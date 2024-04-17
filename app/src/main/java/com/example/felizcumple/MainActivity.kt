package com.example.felizcumple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.felizcumple.ui.theme.FelizCumpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FelizCumpleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}


@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        fontSize = 100.sp

    )
}


/**
 * Esta función define una vista previa de una tarjeta de cumpleaños personalizada.
 * La tarjeta muestra un saludo de cumpleaños para un destinatario específico en este caso Miguel.
 */
@Preview(showBackground = true)
@Composable
fun CartaCumplePreview() {
    FelizCumpleTheme {
        GreetingText(message = "Feliz Cumple Juan")
    }
}