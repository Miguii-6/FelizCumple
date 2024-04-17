# Simón Dice en Kotlin
## ¡Bienvenido al juego de Simón Dice implementado en Kotlin!

Este proyecto es una implementación del clásico juego de Simón Dice, donde el jugador debe repetir una secuencia
creciente de colores. El objetivo es recordar correctamente la secuencia y seguir avanzando en el juego sin cometer
errores.

## Clase data
Empezaremos con todas las variables que usaremos:

- Data: Un objeto singleton que almacena los datos del juego, incluyendo la ronda actual, el estado de reproducción, las
  secuencias del usuario y el bot, el récord, el estado del juego, los colores.

```kotlin
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

```

Al poner todas las variables poco a poco falta los enum de `State` donde estan los estados del juego, el enum de `MyColors`
donde estan los colores usados y de ahí los errores.


- **MyColors:** Una enumeración que define los colores utilizados en el juego como valores con estado mutable (MutableState<Color>).
```kotlin 

// Enumeración que define los colores del juego
enum class MyColors(val color: MutableState<Color>) {
    BLUE(mutableStateOf(Color.Blue)),
    GREEN(mutableStateOf(Color.Green)),
    RED(mutableStateOf(Color.Red)),
    YELLOW(mutableStateOf(Color.Yellow))
}

```

- **State:** Una enumeración que define los posibles estados del juego, como el inicio, la visualización de secuencias, la
  espera de entrada, la entrada del jugador, la verificación y la finalización del juego.
```kotlin
// Enumeración que define los estados del juego
enum class State {
    START,
    SEQUENCE,
    WAITING,
    INPUT,
    CHECKING,
    FINISH
}

```

- **DataContext:** Un objeto que proporciona el contexto de la aplicación, necesario para cargar
  recursos.

```kotlin
// Objeto que proporciona el contexto de la aplicación
object DataContext {
    // Contexto de la aplicación (inicializado posteriormente)
    lateinit var context: Context

    // Función para inicializar el contexto
    fun init(context: Context) {
        this.context = context
    }
}
```

## Clase MyViewModel

La clase MyViewModel es un componente crucial en la implementación del juego "Simón Dice" en Kotlin con Android. Este
ViewModel está diseñado para gestionar la lógica del juego y mantener el estado de los datos necesarios para su funcionamiento.

Empezaremos importando `import androidx.lifecycle.ViewModel` no manualmente si no al que te de error al poner al lado del
nombre la clase `: ViewModel()`.

### Funciones

- Función para generar un número aleatorio:

```kotlin
    fun generateRandomNumber(max: Int): Int {
        return (0..max-1).random()
    }
```

- Función para reiniciar la ronda:

```kotlin
    fun resetRound() {
        Data.round.value = 0
    }
```

- Función para reiniciar la secuencia del usuario:

```kotlin
    fun resetUserSecuence() {
        Data.UserSecuence.clear()
    }
```

- Función para reiniciar la secuencia del bot:

```kotlin
    fun resetBotSecuence() {
        Data.botSecuence.clear()
    }
```

- Función para empezar el juego y reiniciarlo, esta es una que llama a las funciones anteriores:

```kotlin

    fun initGame() {
        resetRound()
        resetUserSecuence()
        resetBotSecuence()
        Data.state = State.START
    }
```

- Función para iluminar el color del juego:

```kotlin
fun lightenColor(color: Color, factor: Float): Color {
        val r = (color.red * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        val g = (color.green * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        val b = (color.blue * 255 * (1 - factor) / 255 + factor).coerceIn(0f, 1f)
        return Color(r, g, b, color.alpha)
    }
```

- Función para escurecer el color del juego al pulsarlo:

```kotlin
fun darkenColor(color: Color, factor: Float): Color {
        val r = (color.red * (1 - factor)).coerceIn(0f, 1f)
        val g = (color.green * (1 - factor)).coerceIn(0f, 1f)
        val b = (color.blue * (1 - factor)).coerceIn(0f, 1f)
        return Color(r, g, b, color.alpha)
    }
```

- Función que muestra la secuencia del bot al usuario

```kotlin
    fun showBotSequence() {
        viewModelScope.launch {
            for (colorIndex in Data.botSecuence) {
                Data.colorFlag = Data.colors[colorIndex].value
                Data.colorsMyColors[colorIndex].color.value = darkenColor(Data.colorFlag, 0.5f)
                Data.sounds[colorIndex].start()
                delay(500L)
                Data.colorsMyColors[colorIndex].color.value = Data.colorFlag
                delay(250L)
            }
            Data.state = State.WAITING
            Log.d("ESTADO", Data.state.toString())
        }
        Log.d("ESTADO", Data.botSecuence.toString())
    }
```
- Función principal `increaseShowBotSecuence` que llama a otras funciones, esta incrementa la secuencia del bot y muestra los colores al usuario.
Una de las que llama es `addBotSecuence` que añade el número a la secuencia del bot y después la función `showSecuence` que es la muestra la secuencia
que a su vez llama a la anterior función `showBotSequence`
```kotlin

    fun increaseShowBotSecuence() {
        Data.state = State.SEQUENCE
        Log.d("ESTADO", Data.state.toString())
        addBotSecuence()
        showSecuence()
    }
    fun addBotSecuence() {
        Data.botSecuence.add(generateRandomNumber(4))
    }

    fun showSecuence() = runBlocking {
        showBotSequence()
    }

```

- Función para la pulsación del botón de color por parte del user:

```kotlin

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
```

- Función que incrementa la secuencia del usuario:

```kotlin
    fun increaseUserSecuence(color: Int) {
        Data.state = State.INPUT
        Log.d("ESTADO", Data.state.toString())
        Data.UserSecuence.add(color)
        Data.state = State.WAITING
        Log.d("ESTADO", Data.state.toString())
    }
```

- Función para saber si al usuario le da al boton correcto incrementa a ronda e reinicia a secuencia e actualiza o record si e necesario e si non e correcta acaba o xogo

```kotlin
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
```

- Función para recoller a ronda actual:

```kotlin
    fun getRound(): Int {
        return Data.round.value
    }
```
- Función que recolle o récord do usuario:

```kotlin
    fun getRecord(): Int {
        return Data.record.value
    }
```

- Función para cambiar el estado del juego

```kotlin
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

```

- Función para obtener el estado de reproducción del juego

```kotlin
    fun getPlayStatus(): String {
        return Data.playStatus.value
    }
```

