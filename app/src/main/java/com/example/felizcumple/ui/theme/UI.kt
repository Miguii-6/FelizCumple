package com.example.felizcumple.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.Column
import com.example.felizcumple.MyViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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