import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    // Cargar datos desde archivos
    Biblioteca.bibliotecas.addAll(FileManager.cargarBibliotecasDeArchivo("src/main/resources/datos_biblioteca.txt"))
    Libro.libros.addAll(FileManager.cargarLibrosDeArchivo("src/main/resources/datos_libros.txt"))

    while (true) {
        println("\n================================================================================================================================================")
        println("\nSeleccione una opción:")
        println("1. Agregar Biblioteca")
        println("2. Listar Bibliotecas")
        println("3. Actualizar Biblioteca")
        println("4. Eliminar Biblioteca")
        println("5. Agregar Libro")
        println("6. Listar Libros")
        println("7. Actualizar Libro")
        println("8. Eliminar Libro")
        println("9. Listar Libros de una Biblioteca")
        println("10. Guardar")
        println("11. Salir")
        print("Opción: ")

        when (scanner.nextInt()) {
            1 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el nombre de la biblioteca: ")
                val nombre = scanner.nextLine()
                print("Ingrese la ubicación de la biblioteca: ")
                val ubicacion = scanner.nextLine()
                print("Ingrese la fecha de creación (dd/MM/yyyy): ")
                val fechaCreacion = SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine())
                print("Ingrese el presupuesto anual: ")
                val presupuestoAnual = scanner.nextDouble()
                print("Es pública? (true/false): ")
                val esPublica = scanner.nextBoolean()
                val biblioteca = Biblioteca(nombre, ubicacion, fechaCreacion, presupuestoAnual, esPublica)
                Biblioteca.agregarBiblioteca(biblioteca)
                scanner.nextLine() // Consumir la línea pendiente después de leer booleano
            }

            2 -> {
                println("\nBibliotecas:")
                Biblioteca.listarBibliotecas().forEach { println(it) }
            }
            3 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el nombre de la biblioteca a actualizar: ")
                val nombre = scanner.nextLine()
                val biblioteca = Biblioteca.obtenerBiblioteca(nombre)
                if (biblioteca != null) {
                    print("Ingrese la nueva ubicación de la biblioteca: ")
                    val ubicacion = scanner.nextLine()
                    print("Ingrese la nueva fecha de creación (dd/MM/yyyy): ")
                    val fechaCreacion = SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine())
                    print("Ingrese el nuevo presupuesto anual: ")
                    val presupuestoAnual = scanner.nextDouble()
                    print("Es pública? (true/false): ")
                    val esPublica = scanner.nextBoolean()
                    val bibliotecaActualizada = Biblioteca(nombre, ubicacion, fechaCreacion, presupuestoAnual, esPublica)
                    Biblioteca.actualizarBiblioteca(nombre, bibliotecaActualizada)
                    scanner.nextLine() // Consumir la línea pendiente después de leer booleano
                } else {
                    println("Biblioteca no encontrada")
                }
            }

            4 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el nombre de la biblioteca a eliminar: ")
                val nombre = scanner.nextLine()
                Biblioteca.eliminarBiblioteca(nombre)
            }
            5 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el nombre de la biblioteca donde se agregará el libro: ")
                val nombreBiblioteca = scanner.nextLine()
                val biblioteca = Biblioteca.obtenerBiblioteca(nombreBiblioteca)
                if (biblioteca != null) {
                    print("Ingrese el id del libro: ")
                    val id = scanner.nextInt()
                    scanner.nextLine() // Consumir la línea pendiente
                    print("Ingrese el título del libro: ")
                    val titulo = scanner.nextLine()
                    print("Ingrese el autor del libro: ")
                    val autor = scanner.nextLine()
                    print("Ingrese el año de publicación: ")
                    val anioPublicacion = scanner.nextInt()
                    print("Ingrese el precio del libro: ")
                    val precio = scanner.nextDouble()
                    print("Está disponible? (true/false): ")
                    val disponible = scanner.nextBoolean()
                    val libro = Libro(id, titulo, autor, anioPublicacion, precio, disponible, nombreBiblioteca)
                    Libro.agregarLibro(libro)
                } else {
                    println("Biblioteca no encontrada")
                }
            }
            6 -> {
                println("\nLibros:")
                Libro.listarLibros().forEach { println(it) }
            }
            7 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el ID del libro a actualizar: ")
                val id = scanner.nextInt()
                val libro = Libro.obtenerLibro(id)
                if (libro != null) {
                    scanner.nextLine() // Consumir la línea pendiente
                    print("Ingrese el nuevo título del libro: ")
                    val titulo = scanner.nextLine()
                    print("Ingrese el nuevo autor del libro: ")
                    val autor = scanner.nextLine()
                    print("Ingrese el nuevo año de publicación: ")
                    val anioPublicacion = scanner.nextInt()
                    scanner.nextLine() // Consumir la línea pendiente
                    print("Ingrese el nuevo precio del libro: ")
                    val precio = scanner.nextDouble()
                    print("Está disponible? (true/false): ")
                    val disponible = scanner.nextBoolean()
                    scanner.nextLine() // Consumir la línea pendiente
                    print("Ingrese el nombre de la biblioteca: ")
                    val bibliotecaNombre = scanner.nextLine()
                    val libroActualizado = Libro(id, titulo, autor, anioPublicacion, precio, disponible, bibliotecaNombre)
                    Libro.actualizarLibro(id, libroActualizado)
                } else {
                    println("Libro no encontrado")
                }
            }

            8 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el ID del libro a eliminar: ")
                val id = scanner.nextInt()
                Libro.eliminarLibro(id)
            }
            9 -> {
                scanner.nextLine() // Consumir la línea pendiente si hay alguna
                print("\nIngrese el nombre de la biblioteca: ")
                val nombreBiblioteca = scanner.nextLine()
                val biblioteca = Biblioteca.obtenerBiblioteca(nombreBiblioteca)
                if (biblioteca != null) {
                    println("\nLibros de la biblioteca $nombreBiblioteca:")
                    val libros = Libro.listarLibrosPorBiblioteca(nombreBiblioteca)
                    if (libros.isNotEmpty()) {
                        libros.forEach { println(it) }
                    } else {
                        println("No hay libros en esta biblioteca.")
                    }
                } else {
                    println("Biblioteca no encontrada.")
                }
            }

            10 -> {
                FileManager.guardarBibliotecasEnArchivo(Biblioteca.listarBibliotecas(), "src/main/resources/datos_biblioteca.txt")
                FileManager.guardarLibrosEnArchivo(Libro.listarLibros(), "src/main/resources/datos_libros.txt")
            }
            11 -> {
                println("Saliendo del programa...")
                break
            }
            else -> println("Opción no válida")
        }
    }
}
