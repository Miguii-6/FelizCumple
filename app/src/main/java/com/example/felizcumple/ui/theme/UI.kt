package com.example.felizcumple.ui.theme

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import com.example.felizcumple.MyViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.felizcumple.Data
import com.example.felizcumple.MyColors
import com.example.felizcumple.R
import com.example.felizcumple.State

var ctxt: Context? = null


/**
 * Composable que define la interfaz de usuario principal del juego "Simón Dice".
 * @param miViewModel ViewModel que contiene la lógica del juego.
 */
@Composable
fun UserInterface(miViewModel: MyViewModel) {
    ctxt = LocalContext.current
    miViewModel
    Column {
        round(miViewModel)
        botonesSimon(miViewModel)
        startIncreaseRound(miViewModel)
    }
}

/**
 * Composable que muestra la sección de la ronda actual y el récord del juego.
 * @param myViewModel ViewModel que contiene los datos del juego.
 */
@Composable
fun round(myViewModel: MyViewModel){
    Column {
        // Fila con el texto "Record" y "Round"
        Row {
            Text(
                modifier = Modifier.padding(20.dp,0.dp,0.dp,0.dp),
                text = stringResource(id = R.string.record)
            )
            Text(
                modifier = Modifier.padding(240.dp,0.dp,0.dp,0.dp),
                text = stringResource(id = R.string.round)
            )
        }
        // Fila con el récord y la ronda actual
        Row {
            Text(
                modifier = Modifier.padding(20.dp,0.dp,0.dp,0.dp),
                text = "${myViewModel.getRecord()}",
                fontSize = 25.sp
            )
            Text(
                text = "${myViewModel.getRound()}",
                modifier = Modifier.padding(if(myViewModel.getRound()<10) 295.dp else 290.dp,0.dp,0.dp,0.dp),
                fontSize = 25.sp
            )
        }
    }
}

/**
 * Composable que muestra los botones de colores del juego "Simón Dice".
 * @param myViewModel ViewModel que contiene la lógica del juego.
 */
@Composable
fun botonesSimon(myViewModel: MyViewModel){
    Row (modifier = Modifier.padding(0.dp,100.dp,0.dp,0.dp)){
        columnButtonSimon(color = MyColors.BLUE.color,myViewModel)
        columnButtonSimon(color = MyColors.GREEN.color, myViewModel)
    }
    Row (){
        columnButtonSimon(color = MyColors.RED.color, myViewModel)
        columnButtonSimon(color = MyColors.YELLOW.color, myViewModel)
    }
}
/**
 * Composable que define un botón de color para el juego "Simón Dice".
 * @param color Color del botón.
 * @param myViewModel ViewModel que contiene la lógica del juego.
 */
@Composable
fun columnButtonSimon(color: MutableState<Color>, myViewModel: MyViewModel){
    Column {
        Button(
            onClick = {
                // Maneja la lógica de interacción cuando se presiona un botón de color
                if (Data.state != State.SEQUENCE && Data.state != State.INPUT && !myViewModel.getPlayStatus().equals("Start")) {
                    myViewModel.increaseUserSecuence(Data.colors.indexOf(color))
                    myViewModel.showButtonPressed(color)
                }
            },
            shape = RectangleShape,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .padding(50.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(color.value)
        ){
            // Contenido del botón (puede ser una imagen u otro contenido)
        }
    }
}
/**
 * Composable que muestra los controles de inicio y aumento de ronda del juego "Simón Dice".
 * @param miViewModel ViewModel que contiene la lógica del juego.
 */
@Composable
fun startIncreaseRound(miViewModel: MyViewModel) {
    Row {
        Column {
            Button(
                onClick = {
                    // Cambia el estado de reproducción del juego
                    miViewModel.changePlayStatus()
                },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(50.dp, 50.dp)
            ) {
                Text(
                    text = miViewModel.getPlayStatus(), textAlign = TextAlign.Center
                )
            }
        }
        Column {
            Button(
                onClick = {
                    // Comprueba la secuencia introducida por el usuario cuando se presiona el botón de reproducción
                    if (miViewModel.getPlayStatus().equals("Start")) {
                        // No hace nada si el juego está en modo "Start"
                    } else {
                        // Comprueba la secuencia del usuario si no está en modo de secuencia o entrada
                        if (Data.state != State.SEQUENCE && Data.state != State.INPUT) {
                            miViewModel.checkSecuence()
                        }
                    }
                },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(50.dp, 50.dp)
            ) {
            }
        }
    }
}