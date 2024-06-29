import java.text.SimpleDateFormat
import java.util.*

class Biblioteca(
    var nombre:String,
    var ubicacion:String,
    var fechaCreacion:Date,
    var presupuestoAnual:Double,
    var esPublica:Boolean
) {
    fun agregarLibros(nuevosLibros:MutableList<Libro>){
        libros = nuevosLibros
    }

    companion object {
        private val bibliotecas = mutableListOf<Biblioteca>()
        private var libros: MutableList<Libro> = mutableListOf()

        // Método Create
        fun agregarBiblioteca(biblioteca: Biblioteca) {
            bibliotecas.add(biblioteca)
            println("\nSe ha agregado la biblioteca: " + biblioteca.nombre)
        }
        fun agregarLibrosABiblioteca(nombre:String, nuevosLibros:MutableList<Libro>){
            bibliotecas.forEach{
                if(it.nombre == nombre){
                    it.agregarLibros(nuevosLibros)
                }
            }
        }

        // Método Read
        fun obtenerBiblioteca(nombre: String): Biblioteca? {
            return bibliotecas.find { it.nombre == nombre }
        }

        // Método Update
        fun actualizarBiblioteca(nombre: String, bibliotecaActualizada: Biblioteca){
            val index = bibliotecas.indexOfFirst { it.nombre == nombre }
            return if (index != -1) {
                bibliotecas[index] = bibliotecaActualizada
                println("\nSe ha actualizado la biblioteca: " + bibliotecas[index].nombre)
            } else {
                println("\nNo se ha actualizado ninguna biblioteca")
            }
        }

        // Método Delete
        fun eliminarBiblioteca(nombre:String){
            bibliotecas.forEach{bibliotecaActual: Biblioteca ->
                if(bibliotecaActual.nombre == nombre){
                    bibliotecas.remove(bibliotecaActual)
                    println("\nSe ha eliminado la biblioteca: "+nombre)
                    return
                }
            }
        }


        // Métodos extra
        fun listarBibliotecas(): List<Biblioteca> {
            return bibliotecas
        }
    }

    override fun toString(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
        var texto = "\nNombre: $nombre \t Ubicación: $ubicacion \t Fecha de Creación: ${formatoFecha.format(fechaCreacion)} \t Presupuesto Anual: $presupuestoAnual \t Es Pública: $esPublica" +
                "\nListado de libros:"
        libros.forEach{texto += it.toString()}
        return texto
    }
}