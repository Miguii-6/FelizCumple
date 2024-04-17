package com.example.felizcumple

import androidx.lifecycle.ViewModel

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

}