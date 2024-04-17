package com.example.felizcumple

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.felizcumple.ui.theme.ctxt
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MyViewModel: ViewModel()  {
    /**
     * Genera un número aleatorio
     * @param max número máximo a generar
     * @return número aleatorio
     */
    fun generateRandomNumber(max: Int): Int {
        return (0..max-1).random()
    }

    /**
     * Reinicia la ronda
     */
    fun resetRound() {
        Data.round.value = 0
    }

    /**
     * Reinicia la secuencia del usuario
     */
    fun resetUserSecuence() {
        Data.UserSecuence.clear()
    }

    /**
     * Reinicia la secuencia del bot
     */
    fun resetBotSecuence() {
        Data.botSecuence.clear()
    }

    /**
     * Reinicia y inicia el juego
     */
    fun initGame() {
        resetRound()
        resetUserSecuence()
        resetBotSecuence()
        Data.state = State.START
    }

    /**
     * Oscurece el color
     * @param color color a oscurecer
     * @param factor factor de oscurecimiento
     * @return color oscurecido
     */
    fun darkenColor(color: Color, factor: Float): Color {
        val r = (color.red * (1 - factor)).coerceIn(0f, 1f)
        val g = (color.green * (1 - factor)).coerceIn(0f, 1f)
        val b = (color.blue * (1 - factor)).coerceIn(0f, 1f)
        return Color(r, g, b, color.alpha)
    }

    /**
     * Ilumina el color
     * @param color color a iluminar
     * @param factor factor de iluminación
     * @return color iluminado
     */
    fun lightenColor(color: Color, factor: Float): Color {
        val r = (color.red * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        val g = (color.green * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        val b = (color.blue * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        return Color(r, g, b, color.alpha)
    }

    /**
     * Muestra la secuencia del bot al usuario
     */
    fun showBotSequence() {
        viewModelScope.launch {
            for (colorIndex in Data.botSecuence) {
                Data.colorFlag = Data.colors[colorIndex].value
                Data.colorsMyColors[colorIndex].color.value = darkenColor(Data.colorFlag, 0.5f)
                Data.colorsMyColors[colorIndex].color.value = Data.colorFlag
                delay(250L)
            }
            Data.state = State.WAITING
            Log.d("ESTADO", Data.state.toString())
        }
        Log.d("ESTADO", Data.botSecuence.toString())
    }

    /**
     * Incrementa la secuencia del bot y muestra los colores al usuario
     */
    fun increaseShowBotSecuence() {
        Data.state = State.SEQUENCE
        Log.d("ESTADO", Data.state.toString())
        addBotSecuence()
        showSecuence()
    }

    /**
     * Añade un número a la secuencia del bot
     */
    fun addBotSecuence() {
        Data.botSecuence.add(generateRandomNumber(4))
    }

    fun showSecuence() = runBlocking {
        showBotSequence()
    }

    /**
     * Maneja la pulsación de un botón de color por parte del usuario
     * @param color color seleccionado por el usuario
     */
    fun showButtonPressed(color: MutableState<Color>) {
        viewModelScope.launch {
            Data.state = State.INPUT
            Data.colorUserFlag = color.value
            color.value = darkenColor(Data.colorUserFlag, 0.5f)
            delay(250L)
            color.value = Data.colorUserFlag
            Data.state = State.WAITING
        }
    }
    /**
     * Incrementa la secuencia del usuario
     * @param color color introducido por el usuario
     */
    fun increaseUserSecuence(color: Int) {
        Data.state = State.INPUT
        Log.d("ESTADO", Data.state.toString())
        Data.UserSecuence.add(color)
        Data.state = State.WAITING
        Log.d("ESTADO", Data.state.toString())
    }

    /**
     * Comprueba si la secuencia del usuario es correcta
     * Si es correcta, incrementa la ronda, reinicia la secuencia del usuario
     * y actualiza el récord si es necesario
     * Si no es correcta, finaliza el juego
     */
    fun checkSecuence() {
        Data.state = State.CHECKING
        Log.d("ESTADO", Data.state.toString())
        if (Data.UserSecuence == Data.botSecuence) {
            Data.round.value++
            if ((Data.round.value - 1) > Data.record.value) {
                Data.record.value = Data.round.value - 1
            }
            Data.UserSecuence.clear()
            increaseShowBotSecuence()
        } else {
            Data.state = State.FINISH
            Toast.makeText(ctxt, "G A M E   O V E R", Toast.LENGTH_SHORT).show()
            Data.playStatus.value = "Start"
            initGame()
            Log.d("ESTADO", Data.state.toString())
        }
    }

    /**
     * Obtiene la ronda actual
     * @return ronda
     */
    fun getRound(): Int {
        return Data.round.value
    }

    /**
     * Obtiene el récord
     * @return récord
     */
    fun getRecord(): Int {
        return Data.record.value
    }

    /**
     * Cambia el estado de reproducción del juego
     */
    fun changePlayStatus() {
        if (Data.playStatus.value == "Start") {
            Data.playStatus.value = "Reset"
            Data.round.value++
            increaseShowBotSecuence()
        } else {
            // Si no se encuentra en estado SEQUENCE o INPUT, se restablece el juego
            if (Data.state != State.SEQUENCE && Data.state != State.INPUT) {
                Data.playStatus.value = "Start"
                initGame()
            }
        }
    }

    /**
     * Obtiene el estado de reproducción del juego
     * @return estado de reproducción
     */
    fun getPlayStatus(): String {
        return Data.playStatus.value
    }





}