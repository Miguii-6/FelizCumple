package com.example.felizcumple

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
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



}