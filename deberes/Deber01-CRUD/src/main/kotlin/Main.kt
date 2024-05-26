import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

fun main() {
    eliminarArchivo()

    // Crear una instancia de Biblioteca
    val fechaCreacion = Date() // Asume la fecha actual
    val biblioteca = Biblioteca(
        nombre = "Biblioteca Central",
        ubicacion = "Calle Principal 123",
        fechaCreacion = fechaCreacion,
        presupuestoAnual = 50000.0,
        esPublica = true
    )

    // Agregar la biblioteca
    Biblioteca.agregarBiblioteca(biblioteca)
    println("___________________________________________________________________")

    // Crear algunos libros
    val libro1 = Libro(id = 1, titulo = "1984", autor = "George Orwell", anioPublicacion = 1949, precio = 19.99, disponible = true)
    val libro2 = Libro(id = 2, titulo = "Cien Años de Soledad", autor = "Gabriel García Márquez", anioPublicacion = 1967, precio = 25.99, disponible = true)

    // Agregar libros
    Libro.agregarLibro(libro1)
    Libro.agregarLibro(libro2)
    println("___________________________________________________________________")

    // Agregar libros a la biblioteca
    Biblioteca.agregarLibrosABiblioteca("Biblioteca Central", Libro.listarLibros())

    // Listar bibliotecas
    println("\nBibliotecas:")
    Biblioteca.listarBibliotecas().forEach { println(it.toString()) }
    println("___________________________________________________________________")

    // Listar libros
    println("\nLibros:")
    Libro.listarLibros().forEach { println(it) }
    println("___________________________________________________________________")

    // Obtener un libro por ID
    val libroObtenido = Libro.obtenerLibro(1)
    println("\nLibro obtenido: $libroObtenido")
    println("___________________________________________________________________")

    // Actualizar un libro
    val libroActualizado = Libro(id = 1, titulo = "1984 (Actualizado)", autor = "George Orwell", anioPublicacion = 1949, precio = 18.99, disponible = true)
    Libro.actualizarLibro(1, libroActualizado)
    println("___________________________________________________________________")

    // Listar libros después de la actualización
    println("\nLibros después de la actualización:")
    Libro.listarLibros().forEach { println(it) }
    println("___________________________________________________________________")

    // Guardar datos en un archivo
    guardarDatosEnArchivo("\nAntes de la eliminacion")

    // Eliminar un libro
    Libro.eliminarLibro(2)
    println("___________________________________________________________________")

    // Listar libros después de la eliminación
    println("\nLibros después de la eliminación:")
    Libro.listarLibros().forEach { println(it) }
    println("___________________________________________________________________")

    // Obtener una biblioteca por nombre
    val bibliotecaObtenida = Biblioteca.obtenerBiblioteca("Biblioteca Central")
    println("\nBiblioteca obtenida: ${bibliotecaObtenida?.toString()}")
    println("___________________________________________________________________")

    // Actualizar una biblioteca
    val bibliotecaActualizada = Biblioteca(
        nombre = "Biblioteca Central",
        ubicacion = "Avenida Principal 456",
        fechaCreacion = fechaCreacion,
        presupuestoAnual = 60000.0,
        esPublica = true
    )
    Biblioteca.actualizarBiblioteca("Biblioteca Central", bibliotecaActualizada)
    println("___________________________________________________________________")

    // Listar bibliotecas después de la actualización
    println("\nBibliotecas después de la actualización:")
    Biblioteca.listarBibliotecas().forEach { println(it.toString()) }
    println("___________________________________________________________________")

    // Guardar datos en un archivo
    guardarDatosEnArchivo("\nDespues de eliminar libros y actualizar biblioteca")

    // Eliminar una biblioteca
    Biblioteca.eliminarBiblioteca("Biblioteca Central")
    println("___________________________________________________________________")

    // Listar bibliotecas después de la eliminación
    println("\nBibliotecas después de la eliminación:")
    Biblioteca.listarBibliotecas().forEach { println(it.toString()) }

    // Guardar datos en un archivo
    guardarDatosEnArchivo("\nDespues de eliminar biblioteca")
}

fun eliminarArchivo(){
    val nombreArchivo = "src/main/resources/datos_biblioteca.txt"

    // Eliminar el archivo si ya existe
    val archivo = File(nombreArchivo)
    if(archivo.exists()) {
        archivo.delete()
    }
}
fun guardarDatosEnArchivo(encabezado: String) {
    val nombreArchivo = "src/main/resources/datos_biblioteca.txt"

    // Crear un nuevo archivo y escribir los datos
    BufferedWriter(FileWriter(nombreArchivo, true)).use { writer ->
        writer.write(encabezado)
        Biblioteca.listarBibliotecas().forEach { biblioteca ->
            writer.write(biblioteca.toString())
            writer.write("\n")
        }
    }
    println("\nDatos guardados en el archivo")
}
