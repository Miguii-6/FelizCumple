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
}