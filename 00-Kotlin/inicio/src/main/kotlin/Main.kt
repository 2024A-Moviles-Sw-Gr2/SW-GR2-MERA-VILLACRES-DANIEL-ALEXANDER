import java.util.*
import java.util.function.IntConsumer

fun main(){
    println("Hola mundo")
    // Inmutables (No se pueden re asignar "=")
    val inmutable: String = "Daniel"
    //inmutable = "Mera" // ERROR!

    // Mutables
    var mutable: String = "Daniel"
    mutable = "Mera" // OK

    // val > var

    // Duck Typing
    var ejemploVariable = " Daniel Mera "
    val edadEjemplo: Int = 12
    ejemploVariable.trim() // Para borra espacios en blanco
    // ejemploVariable = edadEjemplo // ERROR! tipo incorrecto

    // Variables primitivas
    val nombre: String = "Daniel"
    val sueldo: Double = 1.2
     val estadoCivil: Char = 'S'
    val mayorEdad: Boolean = true
    // clases en Java
    val fechaNacimiento: Date = Date()

    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") ->{
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else ->{
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito


    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00, 20.00)
    // Named parameters
    // calcularSueldo (sueldo, tasa, bonoEspecial)
    calcularSueldo(10.00, bonoEspecial = 20.00)
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)

}

// void -> Unit
fun imprimirNombre (nombre:String): Unit{
    println("Nombre: ${nombre}") // Template Strings
}
fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial: Double? = null // Opcional (nullable)
    // variable? -> "?" Es Nullable (osea que puede en algun momento ser nulo)
):Double {
    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    if (bonoEspecial == null){
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) * bonoEspecial
    }
}


abstract class NumerosJava{
    protected val numeroUno:Int
    private val numeroDos:Int
    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract class Numeros(
    // Caso 1) Parametro normal
    // uno:Int, (Parametro sin modificar acceso)

    // Caso 2) Parametro y propiedad (atributo)(private)
    // private var uno:Int (propiedad "instancia.uno")

    protected val numeroUno: Int, // instancia.numeroUno
    protected val numeroDos: Int // insntancia.numeroDos
){
    init { // bloque de constructor primario (opcional)
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}


class Suma (
    unoParametro: Int, // Parametro
    dosParametro: Int // Parametro
): Numeros( // Clase papa, Numeros (extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito:String = "Explicito" // Publicos (public opcional)
    val soyPublicoImplicito:String = "Implicito" // Publico (propiedades, metodos)
    init{ // Bloque de codigo constructor primario
        // this.unoParametro // Error no existe
        this.numeroUno
        this.numeroDos
        numeroUno // this. Opcional (propiedades, metodos)
        numeroDos // this. Opcional (propiedades, metodos)
        this.soyPublicoExplicito
        soyPublicoImplicito // this. Opcional (propiedades, metodos)
    }

    // public fun sumar():Int (Modificador "puclic" es opcional)
    fun Sumar():Int{
        val total = numeroUno + numeroDos
        // Suma.agregarHistorial(total) ("Suma." o "NombreClase." es opcional)
        agregarHistorial(total)
        return total
    }

    companion object{ // Comparte entre todas las instancias, similar al Static
        // funciones y variables
        val pi = 3.14
        fun elevarAlCuadrado(num:Int):Int{return num*num}
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorTotalSuma:Int){
            historialSumas.add(valorTotalSuma)
        }
    }
}

