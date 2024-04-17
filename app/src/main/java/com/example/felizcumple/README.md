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