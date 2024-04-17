package com.example.felizcumple.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.Column
import com.example.felizcumple.MyViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.felizcumple.MyColors
import com.example.merge.R


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
                    Data.sounds[Data.colors.indexOf(color)].start()
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
