class Libro (
    var id: Int,
    var titulo: String,
    var autor: String,
    var anioPublicacion: Int,
    var precio: Double,
    var disponible: Boolean,
    var bibliotecaNombre: String
) {
    // Métodos CRUD para Libro
    companion object {
        val libros = mutableListOf<Libro>()

        // Método Create
        fun agregarLibro(libro: Libro) {
            libros.add(libro)
            println("\nSe ha creado el libro: " + libro.titulo)
        }

        // Método Read
        fun obtenerLibro(id: Int): Libro? {
            return libros.find { it.id == id }
        }

        // Método Update
        fun actualizarLibro(id: Int, libroActualizado: Libro) {
            val index = libros.indexOfFirst { it.id == id }
            return if (index != -1) {
                libros[index] = libroActualizado
                println("\nSe ha actualizado el libro: " + libros[index].titulo)
            } else {
                println("\nNo se ha actualizado ningún libro")
            }
        }

        // Método Delete
        fun eliminarLibro(id: Int) {
            libros.forEach { libroActual ->
                if (libroActual.id == id) {
                    libros.remove(libroActual)
                    println("\nSe ha eliminado el libro con id: " + id)
                    return
                }
            }
        }


        // Métodos extra
        fun listarLibros(): MutableList<Libro> {
            return libros
        }

        fun listarLibrosPorBiblioteca(bibliotecaNombre: String): List<Libro> {
            return libros.filter { it.bibliotecaNombre.lowercase() == bibliotecaNombre.lowercase() }
        }

    }
    override fun toString(): String {
        return "\nID: $id \t Título: $titulo \t Autor: $autor \t Año publicación: $anioPublicacion \t Precio: $precio \t Disponible: $disponible"
    }
}