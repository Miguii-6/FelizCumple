## App de feliz cumpleaños (tutorial)

Esta sera una app para Android que muestra un saludo de cumpleaños en formato de texto, que, una vez creada, se verá
como esta captura de pantalla. Desde el código base al crear un nuevo proyecto en AndroidStudio. Este es el código que
modificaremos para realizar la app.


```kotlin

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
````
**Sugerencia** usar en la parte de arriba a la derecha la opción de split dado que desde ahí iremos viendo como avanza
el proyecto.

![Split](Imagenes/img.png)

Así es el proyecto en zona de split en la ventana de la derecha para como es al inicio:

![Inicio](Imagenes/img_1.png)

### Primeros cambios:

En esta parte del código:

```kotlin
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FelizCumpleTheme {
        Greeting("Android")
    }
}
```

Cambiaremos el nombre de la función a uno acorde a nuestro nombre del proyecto en mi caso a CartaCumple y en el apartado
de `Greeeting` lo de dentro de parentesis a tu nombre y se quedaría así:

```kotlin
@Preview(showBackground = true)
@Composable
fun CartaCumplePreview() {
    FelizCumpleTheme {
        Greeting("Miguel")
    }
}
```

Resultado al realizar el cambio en la parte de la ventana de la derecha del diseño Split anteriormente comentado:

![Miguel](Imagenes/img_2.png)

### Nuevo elemento de texto
El siguiente cambio que haremos sera para agregar un nuevo elemento de texto para crear un saludo de cumpleaños.

Empezaremos quitando el siguiente código:

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
```

Y ahora al quitar esa función procederemos a quitar los errores. En la función `onCreate()` se llama a esta función
donde también lo eliminaremos. Este es el error de la función `onCreate()`:

![Greeting](Imagenes/img_3.png)

Y también dará error en la función de `CartaCumplePreview()` con el mismo error y también eliminaremos donde se llama
a la función eliminada. Quedará tu archivo de `MainActivity.kt` similar a este código:

```kotlin
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
                }
            }
        }
    }
}



/**
 * Esta función define una vista previa de una tarjeta de cumpleaños personalizada.
 * La tarjeta muestra un saludo de cumpleaños para un destinatario específico en este caso Miguel.
 */
@Preview(showBackground = true)
@Composable
fun CartaCumplePreview() {
    FelizCumpleTheme {
    }
}
```

Para añadir el elemento de texto encima de la función `CartaCumplePreview()`, agregaremos una nueva función llamada
`GreetingText()`. Importante agrega también el `@Composable` antes de la función, ya que esta función de Compose será
un elemento componible `Text`. Así seria como estaría el código agregado:

```kotlin 
@Composable
fun GreetingText() {
}
```

Seguiremos añadiendo al elemento componible entre los parentesis un parámetro `Modifier` y pase ese `modifier`  a su
primer elemento secundario.

```kotlin 
@Composable
fun GreetingText(modifier: Modifier = Modifier) {
}
```

Procederemos al mensage agregando un parámetro `message` de tipo `String` a esta misma función.

```kotlin
@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
}
```

Seguiremos con la misma función agregando un elemento `Text` componible para que pase el mensaje  de texto como un
argumento con nombre.

```kotlin
@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message
    )
}
```

Para ver una vista previa de la función `GreetingText()` en el panel Split en la ventana derecha.

- Llamaremos a la función de `GreetingText()` dentro de la función de `CartaCumplePreview()`. Seguiremos añadiendo entre
  los parentesis un argumento tipo `String`, seria el mensaje de cumpleaños para tu amigo quedaria asi el código de
  `CartaCumplePreview()`:

```kotlin
@Preview(showBackground = true)
@Composable
fun CartaCumplePreview() {
    FelizCumpleTheme {
        GreetingText(message = "Feliz Cumple Juan")
    }
}
```
