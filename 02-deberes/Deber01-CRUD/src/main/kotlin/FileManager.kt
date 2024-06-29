import java.io.File
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class FileManager {

    companion object {
        private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")

        fun guardarBibliotecasEnArchivo(bibliotecas: List<Biblioteca>, nombreArchivo: String) {
            BufferedWriter(FileWriter(nombreArchivo)).use { writer ->
                bibliotecas.forEach { biblioteca ->
                    val linea = "${biblioteca.nombre};${biblioteca.ubicacion};${formatoFecha.format(biblioteca.fechaCreacion)};${biblioteca.presupuestoAnual};${biblioteca.esPublica}"
                    writer.write(linea)
                    writer.newLine()
                }
            }
            println("\nDatos de bibliotecas guardados en el archivo")
        }

        fun cargarBibliotecasDeArchivo(nombreArchivo: String): List<Biblioteca> {
            val bibliotecas = mutableListOf<Biblioteca>()
            val archivo = File(nombreArchivo)

            if (archivo.exists()) {
                BufferedReader(FileReader(nombreArchivo)).use { reader ->
                    var linea = reader.readLine()
                    while (linea != null) {
                        val datos = linea.split(";")
                        val nombre = datos[0]
                        val ubicacion = datos[1]
                        val fechaCreacion = formatoFecha.parse(datos[2])
                        val presupuestoAnual = datos[3].toDouble()
                        val esPublica = datos[4].toBoolean()
                        val biblioteca = Biblioteca(nombre, ubicacion, fechaCreacion, presupuestoAnual, esPublica)
                        bibliotecas.add(biblioteca)
                        linea = reader.readLine()
                    }
                }
                println("\nDatos de bibliotecas cargados del archivo")
            }
            return bibliotecas
        }

        fun guardarLibrosEnArchivo(libros: List<Libro>, nombreArchivo: String) {
            BufferedWriter(FileWriter(nombreArchivo)).use { writer ->
                libros.forEach { libro ->
                    val linea = "${libro.id};${libro.titulo};${libro.autor};${libro.anioPublicacion};${libro.precio};${libro.disponible};${libro.bibliotecaNombre}"
                    writer.write(linea)
                    writer.newLine()
                }
            }
            println("\nDatos de libros guardados en el archivo")
        }

        fun cargarLibrosDeArchivo(nombreArchivo: String): List<Libro> {
            val libros = mutableListOf<Libro>()
            val archivo = File(nombreArchivo)

            if (archivo.exists()) {
                BufferedReader(FileReader(nombreArchivo)).use { reader ->
                    var linea = reader.readLine()
                    while (linea != null) {
                        val datos = linea.split(";")
                        val id = datos[0].toInt()
                        val titulo = datos[1]
                        val autor = datos[2]
                        val anioPublicacion = datos[3].toInt()
                        val precio = datos[4].toDouble()
                        val disponible = datos[5].toBoolean()
                        val bibliotecaNombre = datos[6]
                        val libro = Libro(id, titulo, autor, anioPublicacion, precio, disponible, bibliotecaNombre)
                        libros.add(libro)
                        linea = reader.readLine()
                    }
                }
                println("\nDatos de libros cargados del archivo")
            }
            return libros
        }
    }
}
