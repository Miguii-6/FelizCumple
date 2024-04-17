package com.example.felizcumple

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

/**
 * Clase que contiene los datos del juego
 * @property round ronda actual
 * @property roundState estado de la ronda
 * @property userSequence secuencia del usuario introducida por el usuario
 * @property botSequence secuencia del bot generada por el bot
 * @property record récord del juego
 */

object Data {
    var round = mutableStateOf(0)
    var playStatus = mutableStateOf("Start")
    var UserSecuence = mutableListOf<Int>()
    var botSecuence = mutableListOf<Int>()
    var record = mutableStateOf(0)
    var state = State.START
    var colors = listOf(
        MyColors.BLUE.color,
        MyColors.GREEN.color,
        MyColors.RED.color,
        MyColors.YELLOW.color
    )
    var colorsMyColors = MyColors.values()
    var colorFlag: Color = Color.White
    var colorUserFlag: Color = Color.White
}


// Enumeración que define los colores del juego
enum class MyColors(val color: MutableState<Color>) {
    BLUE(mutableStateOf(Color.Blue)),
    GREEN(mutableStateOf(Color.Green)),
    RED(mutableStateOf(Color.Red)),
    YELLOW(mutableStateOf(Color.Yellow))
}

// Enumeración que define los estados del juego
enum class State {
    START,
    SEQUENCE,
    WAITING,
    INPUT,
    CHECKING,
    FINISH
}
// Objeto que proporciona el contexto de la aplicación
object DataContext {
    // Contexto de la aplicación (inicializado posteriormente)
    lateinit var context: Context

    // Función para inicializar el contexto
    fun init(context: Context) {
        this.context = context
    }
}