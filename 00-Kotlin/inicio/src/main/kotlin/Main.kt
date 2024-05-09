import java.util.*

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


}