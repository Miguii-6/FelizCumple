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
