# Simón Dice en Kotlin
## ¡Bienvenido al juego de Simón Dice implementado en Kotlin!

Este proyecto es una implementación del clásico juego de Simón Dice, donde el jugador debe repetir una secuencia
creciente de colores. El objetivo es recordar correctamente la secuencia y seguir avanzando en el juego sin cometer
errores.

## Índice

1. [Clase Data](#clase-data)
2. [Clase MyViewModel](#clase-myviewmodel)
  - [Funciones](#funciones)
3. [Clase UI](#clase-ui)
4. [Clase MainActivity](#clase-mainactivity)
5. [UI a MVVC](#pasar-a-MVVC)


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

## Clase UI

la clase ui proporciona las funciones Compose necesarias para representar la interfaz gráfica del 
juego "Simón Dice", permitiendo al usuario interactuar con la aplicación a través de botones de 
colores y controles de juego. Esta clase es fundamental para la visualización y manipulación de la 
interfaz de usuario dentro del entorno de Jetpack Compose.


la variable `ctxt` se utiliza para almacenar y acceder al contexto de la aplicación en el entorno de 
Jetpack Compose, permitiendo interactuar con recursos y servicios de Android de manera segura y 
eficiente dentro de la interfaz de usuario del juego "Simón Dice".

- Función composable que define la interfaz de usuario principal del juego:

```kotlin
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
```

- Función composable que muestra la sección de la ronda actual y el récord del juego

```

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
```

- Función composable que muestra los botones de colores del juego

```kotlin
@Composable
fun botonesSimon(myViewModel: MyViewModel){
  Row (modifier = Modifier.padding(0.dp,100.dp,0.dp,0.dp)){
    columnButtonSimon(color = MyColors.BLUE.color,myViewModel)
    columnButtonSimon(color = MyColors.GREEN.color, myViewModel)
  }
  Row (){
    columnButtonSimon(color = MyColors.RED.color, myViewModel)
    columnButtonSimon(color = MyColors.YELLOW.color, myViewModel)
  }

```

- Función composable que define un boton de color:

```kotlin

@Composable
fun columnButtonSimon(color: MutableState<Color>, myViewModel: MyViewModel){
    Column {
        Button(
            onClick = {
                // Maneja la lógica de interacción cuando se presiona un botón de color
                if (Data.state != State.SEQUENCE && Data.state != State.INPUT && !myViewModel.getPlayStatus().equals("Start")) {
                    myViewModel.increaseUserSecuence(Data.colors.indexOf(color))
                    Data.sounds[Data.colors.indexOf(color)].start()
                    myViewModel.showButtonPressed(color)
                }
            },
            shape = RectangleShape,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .padding(50.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(color.value)
        ){
            // Contenido del botón (puede ser una imagen u otro contenido)
        }
    }
}

```

- Función composable para el botón de inicio y de aumento de ronda:

```kotlin
@Composable
fun startIncreaseRound(miViewModel: MyViewModel) {
    Row {
        Column {
            Button(
                onClick = {
                    // Cambia el estado de reproducción del juego
                    miViewModel.changePlayStatus()
                },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(50.dp, 50.dp)
            ) {
                Text(
                    text = miViewModel.getPlayStatus(), textAlign = TextAlign.Center
                )
            }
        }
        Column {
            Button(
                onClick = {
                    // Comprueba la secuencia introducida por el usuario cuando se presiona el botón de reproducción
                    if (miViewModel.getPlayStatus().equals("Start")) {
                        // No hace nada si el juego está en modo "Start"
                    } else {
                        // Comprueba la secuencia del usuario si no está en modo de secuencia o entrada
                        if (Data.state != State.SEQUENCE && Data.state != State.INPUT) {
                            miViewModel.checkSecuence()
                        }
                    }
                },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(50.dp, 50.dp)
            ) {
                // Contenido del botón (imagen de reproducción)
                Image(
                    painter = painterResource(id = R.drawable.play_arrow),
                    contentDescription = stringResource(id = R.string.arrowDescription)
                )
            }
        }
    }
}
```

## Clase MainActivity

Por último esta clase procederemos con el código sin modificar que es este del cual procederemos a 
modificar y quitar código:

```kotlin
package com.example.felizcumple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.felizcumple.ui.theme.FelizCumpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FelizCumpleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FelizCumpleTheme {
        Greeting("Android")
    }
}
```

De este código eliminaremos la función de `Greeting`, es decir, esta parte de código:

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
```

Al hacer esto dara error debido a que se llama en la función `onCreate()` y en la función `GreetingPreview`
y eliminaremos esas lineas donde esta llamado a la función eliminada.



Ahora iremos por la función de `GreetingPreview` donde agregaremos la siguiente variable
```kotlin
    var myViewModel = MyViewModel()

```

Donde en la parte de `Theme` agregaremos una `Surface` y también una línea de código para desde la
interfaz del user llamando a `myViewModel` así quedaria el código de `GreetingPreview` entero:
```kotlin

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var myViewModel = MyViewModel()
    FelizCumpleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(255,100,150)
        ) {
            UserInterface(miViewModel = myViewModel)
        }
    }
}
```

Ahora para acabar con el código haremos los cambios pertinentes en la función `onCreate` en esta 
función añadiremos encima de `setContent` las siguientes dos lineas:
```kotlin
DataContext.init(applicationContext)
var myViewModel = MyViewModel()
```
Después cambiaremos en `Surface` el color a en este caso:
```kotlin
color = Color(255,100,150)

```

Y justo en la misma línea donda antes eliminamos la línea de codigo de cuando se llamaba al `Greeting`
ahí pondremos la siguiente línea:
```kotlin
UserInterface(miViewModel = myViewModel)
```

Quedaria como resultado de esta clase con el código completo:

```kotlin
package com.example.felizcumple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.felizcumple.ui.theme.FelizCumpleTheme
import com.example.felizcumple.ui.theme.UserInterface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataContext.init(applicationContext)
        var myViewModel = MyViewModel()
        setContent {
            FelizCumpleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(255,100,150)
                ) {
                    UserInterface(miViewModel = myViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var myViewModel = MyViewModel()
    FelizCumpleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(255,100,150)
        ) {
            UserInterface(miViewModel = myViewModel)
        }
    }
}
```

## Pasar a MVVC

Como yo entregue en la primera entrega con MVVC ya hecho, voy a explicar como es el cambio de tener el código en la UI
todo junto a tener todo bien estructurado en una arquitectura MVVC.
La refactorización implica reestructurar el código para separar la lógica del juego y la gestión de 
datos de la interfaz de usuario utilizando la arquitectura MVVC.



### Paso 1: Definir el Modelo

Creamos la clase `Data` para almacenar los datos del juego y los enums `State` y `MyColors` para 
representar los estados del juego y los colores utilizados.

- **Data:** Almacena los datos del juego, como la ronda actual, el récord, la secuencia del bot y 
del usuario, entre otros.

- **State:** Enumera los posibles estados del juego, como inicio, secuencia, entrada, verificación, etc.

- **MyColors:** Enumera los colores utilizados en el juego, cada uno representado por un objeto con un color mutable.

### Paso 2: Definir el ViewModel

Creamos la clase `MyViewModel`, que extiende `ViewModel`, para manejar la lógica del juego y actuar 
como un puente entre el modelo y la vista. Este ViewModel se encarga de:

- Inicializar el juego.
- Generar números aleatorios.
- Controlar la secuencia del bot.
- Manejar la secuencia del usuario.
- Verificar la secuencia del usuario.
- Actualizar el modelo en respuesta a las interacciones del usuario.

### Paso 3: Definir la Vista (UI)

En esta fase, creamos funciones `@Composable` para definir la interfaz de usuario utilizando 
Jetpack Compose. Estas funciones son responsables de mostrar los datos del juego y manejar las 
interacciones del usuario.

### Paso 4: Configurar MainActivity

Configuramos la `MainActivity` para utilizar el `ViewModel`, el `Controller` y cargar la interfaz de usuario.

Este proceso mejora la estructura del código al separar las preocupaciones y hacer que el código sea 
más modular y fácil de mantener. Cada componente (Data, UI, ViewModel) tiene una 
función claramente definida en el contexto de la arquitectura MVVC, lo que facilita la comprensión y el mantenimiento del código.


