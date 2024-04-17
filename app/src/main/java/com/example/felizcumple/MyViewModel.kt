package com.example.felizcumple

import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color


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


}