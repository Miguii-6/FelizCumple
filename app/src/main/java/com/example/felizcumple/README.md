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

- función para generar un número aleatorio:

```kotlin
/**
     * Genera un número aleatorio
     * @param max número máximo a generar
     * @return número aleatorio
     */
    fun generateRandomNumber(max: Int): Int {
        return (0..max-1).random()
    }
```

