package com.example.deber02_damv.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.deber02_damv.Biblioteca
import com.example.deber02_damv.Libro
import java.util.Date

class SQLiteHelper(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaBiblioteca = """
            CREATE TABLE BIBLIOTECA(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                ubicacion VARCHAR(50),
                fechaCreacion DATE,
                presupuestoAnual DECIMAL(15, 2),
                esPublica BOOLEAN
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaBiblioteca)

        val scriptSQLCrearTablaLibro = """
            CREATE TABLE LIBRO(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(50),
                autor VARCHAR(50),
                anioPublicacion INTEGER,
                precio DECIMAL(15, 2),
                disponible BOOLEAN,
                bibliotecaNombre VARCHAR(50)
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaLibro)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS BIBLIOTECA")
        db?.execSQL("DROP TABLE IF EXISTS LIBRO")
        onCreate(db)
    }

    // Métodos para la tabla BIBLIOTECA
    fun crearBiblioteca(
        nombre: String,
        ubicacion: String,
        fechaCreacion: Date,
        presupuestoAnual: Double,
        esPublica: Boolean
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        val fechaTimestamp = fechaCreacion.time

        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("ubicacion", ubicacion)
        valoresAGuardar.put("fechaCreacion", fechaTimestamp)
        valoresAGuardar.put("presupuestoAnual", presupuestoAnual)
        valoresAGuardar.put("esPublica", esPublica)
        val resultadoGuardar = basedatosEscritura.insert(
            "BIBLIOTECA",
            null,
            valoresAGuardar
        )
        basedatosEscritura.close()
        return resultadoGuardar.toInt() != -1
    }

    fun eliminarBiblioteca(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura.delete(
            "BIBLIOTECA",
            "id=?",
            parametrosConsultaDelete
        )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarBiblioteca(
        id: Int,
        nombre: String,
        ubicacion: String,
        fechaCreacion: Date,
        presupuestoAnual: Double,
        esPublica: Boolean
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()

        val fechaTimestamp = fechaCreacion.time

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("ubicacion", ubicacion)
        valoresAActualizar.put("fechaCreacion", fechaTimestamp)
        valoresAActualizar.put("presupuestoAnual", presupuestoAnual)
        valoresAActualizar.put("esPublica", esPublica)

        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura.update(
            "BIBLIOTECA",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )
        conexionEscritura.close()
        return resultadoActualizacion.toInt() != -1
    }

    fun consultarBibliotecaPorID(id: Int): Biblioteca? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BIBLIOTECA WHERE ID = ?
        """.trimIndent()
        val arregloParametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            arregloParametrosConsultaLectura
        )

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        val arregloRespuesta = arrayListOf<Biblioteca>()
        if (existeAlMenosUno) {
            do {
                val indiceNombre = resultadoConsultaLectura.getColumnIndex("nombre")
                val indiceUbicacion = resultadoConsultaLectura.getColumnIndex("ubicacion")
                val indiceFechaCreacion = resultadoConsultaLectura.getColumnIndex("fechaCreacion")
                val indicePresupuestoAnual = resultadoConsultaLectura.getColumnIndex("presupuestoAnual")
                val indiceEsPublica = resultadoConsultaLectura.getColumnIndex("esPublica")

                val nombreBiblioteca = resultadoConsultaLectura.getString(indiceNombre)
                val ubicacion = resultadoConsultaLectura.getString(indiceUbicacion)
                val fechaCreacionTimestamp = resultadoConsultaLectura.getLong(indiceFechaCreacion)
                val presupuestoAnual = resultadoConsultaLectura.getDouble(indicePresupuestoAnual)
                val esPublica = resultadoConsultaLectura.getInt(indiceEsPublica)

                val fechaCreacion = Date(fechaCreacionTimestamp)
                val esPublicaBoolean = esPublica == 1

                val biblioteca = Biblioteca(
                    nombreBiblioteca,
                    ubicacion,
                    fechaCreacion,
                    presupuestoAnual,
                    esPublicaBoolean
                )
                arregloRespuesta.add(biblioteca)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return if (arregloRespuesta.size > 0) arregloRespuesta[0] else null
    }

    fun obtenerIDBiblioteca(nombre: String): Int? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BIBLIOTECA WHERE NOMBRE = ?
        """.trimIndent()
        val arregloParametrosConsultaLectura = arrayOf(nombre)
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            arregloParametrosConsultaLectura
        )
        if (resultadoConsultaLectura.moveToFirst()) {
            val indiceID = resultadoConsultaLectura.getColumnIndex("id")
            val id = resultadoConsultaLectura.getInt(indiceID)
            resultadoConsultaLectura.close()
            baseDatosLectura.close()
            return id
        } else {
            resultadoConsultaLectura.close()
            baseDatosLectura.close()
            return -1
        }
    }

    fun obtenerIDLibro(nombre:String):Int?{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM LIBRO WHERE TITULO = ?
        """.trimIndent()
        val arregloParametrosConsultaLectura = arrayOf(nombre.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            arregloParametrosConsultaLectura
        )
        if(resultadoConsultaLectura.moveToFirst()){
            val indiceID = resultadoConsultaLectura.getColumnIndex("id")
            val id = resultadoConsultaLectura.getInt(indiceID)
            return id
        }else{
            return -1
        }
    }

    fun consultarListaBiblioteca(): List<Biblioteca> {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BIBLIOTECA
        """.trimIndent()
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            null
        )

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        val arregloRespuesta = arrayListOf<Biblioteca>()
        if (existeAlMenosUno) {
            do {
                val indiceNombre = resultadoConsultaLectura.getColumnIndex("nombre")
                val indiceUbicacion = resultadoConsultaLectura.getColumnIndex("ubicacion")
                val indiceFechaCreacion = resultadoConsultaLectura.getColumnIndex("fechaCreacion")
                val indicePresupuestoAnual = resultadoConsultaLectura.getColumnIndex("presupuestoAnual")
                val indiceEsPublica = resultadoConsultaLectura.getColumnIndex("esPublica")

                val nombreBiblioteca = resultadoConsultaLectura.getString(indiceNombre)
                val ubicacion = resultadoConsultaLectura.getString(indiceUbicacion)
                val fechaCreacionTimestamp = resultadoConsultaLectura.getLong(indiceFechaCreacion)
                val presupuestoAnual = resultadoConsultaLectura.getDouble(indicePresupuestoAnual)
                val esPublica = resultadoConsultaLectura.getInt(indiceEsPublica)

                val fechaCreacion = Date(fechaCreacionTimestamp)
                val esPublicaBoolean = esPublica == 1

                val biblioteca = Biblioteca(
                    nombreBiblioteca,
                    ubicacion,
                    fechaCreacion,
                    presupuestoAnual,
                    esPublicaBoolean
                )
                arregloRespuesta.add(biblioteca)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return arregloRespuesta
    }

    // Métodos para la tabla LIBRO
    fun crearLibro(
        titulo: String,
        autor: String,
        anioPublicacion: Int,
        precio: Double,
        disponible: Boolean,
        bibliotecaNombre: String
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        valoresAGuardar.put("titulo", titulo)
        valoresAGuardar.put("autor", autor)
        valoresAGuardar.put("anioPublicacion", anioPublicacion)
        valoresAGuardar.put("precio", precio)
        valoresAGuardar.put("disponible", disponible)
        valoresAGuardar.put("bibliotecaNombre", bibliotecaNombre)

        val resultadoGuardar = basedatosEscritura.insert(
            "LIBRO",
            null,
            valoresAGuardar
        )
        basedatosEscritura.close()
        return resultadoGuardar.toInt() != -1
    }

    fun eliminarLibro(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura.delete(
            "LIBRO",
            "id=?",
            parametrosConsultaDelete
        )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarLibro(
        id: Int,
        titulo: String,
        autor: String,
        anioPublicacion: Int,
        precio: Double,
        disponible: Boolean
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()

        valoresAActualizar.put("titulo", titulo)
        valoresAActualizar.put("autor", autor)
        valoresAActualizar.put("anioPublicacion", anioPublicacion)
        valoresAActualizar.put("precio", precio)
        valoresAActualizar.put("disponible", disponible)

        val parametrosConsultaActualizar = arrayOf( id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "LIBRO",
                valoresAActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if(resultadoActualizacion.toInt()==-1) false else true
    }

    fun consultarLibroPorID(id: Int): Libro? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM LIBRO WHERE ID = ?
        """.trimIndent()
        val arregloParametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            arregloParametrosConsultaLectura
        )

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        val arregloRespuesta = arrayListOf<Libro>()
        if (existeAlMenosUno) {
            do {
                val indiceTitulo = resultadoConsultaLectura.getColumnIndex("titulo")
                val indiceAutor = resultadoConsultaLectura.getColumnIndex("autor")
                val indiceAnioPublicacion = resultadoConsultaLectura.getColumnIndex("anioPublicacion")
                val indicePrecio = resultadoConsultaLectura.getColumnIndex("precio")
                val indiceDisponible = resultadoConsultaLectura.getColumnIndex("disponible")
                val indiceBibliotecaNombre = resultadoConsultaLectura.getColumnIndex("bibliotecaNombre")

                val tituloLibro = resultadoConsultaLectura.getString(indiceTitulo)
                val autor = resultadoConsultaLectura.getString(indiceAutor)
                val anioPublicacion = resultadoConsultaLectura.getInt(indiceAnioPublicacion)
                val precio = resultadoConsultaLectura.getDouble(indicePrecio)
                val disponible = resultadoConsultaLectura.getInt(indiceDisponible)
                val bibliotecaNombre = resultadoConsultaLectura.getString(indiceBibliotecaNombre)

                val disponibleBoolean = disponible == 1

                val libro = Libro(
                    id,
                    tituloLibro,
                    autor,
                    anioPublicacion,
                    precio,
                    disponibleBoolean,
                    bibliotecaNombre
                )
                arregloRespuesta.add(libro)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return if (arregloRespuesta.size > 0) arregloRespuesta[0] else null
    }

    fun consultarListaLibro(nombreBiblioteca: String): List<Libro> {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM LIBRO 
        WHERE bibliotecaNombre = ?
    """.trimIndent()
        val parametrosConsulta = arrayOf(nombreBiblioteca)
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsulta
        )

        val arregloRespuesta = mutableListOf<Libro>()
        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                // Obtener los índices de las columnas
                val indiceId = resultadoConsultaLectura.getColumnIndexOrThrow("id")
                val indiceTitulo = resultadoConsultaLectura.getColumnIndexOrThrow("titulo")
                val indiceAutor = resultadoConsultaLectura.getColumnIndexOrThrow("autor")
                val indiceAnioPublicacion = resultadoConsultaLectura.getColumnIndexOrThrow("anioPublicacion")
                val indicePrecio = resultadoConsultaLectura.getColumnIndexOrThrow("precio")
                val indiceDisponible = resultadoConsultaLectura.getColumnIndexOrThrow("disponible")
                val indiceBibliotecaNombre = resultadoConsultaLectura.getColumnIndexOrThrow("bibliotecaNombre")

                // Obtener los valores de las columnas
                val id = resultadoConsultaLectura.getInt(indiceId)
                val titulo = resultadoConsultaLectura.getString(indiceTitulo)
                val autor = resultadoConsultaLectura.getString(indiceAutor)
                val anioPublicacion = resultadoConsultaLectura.getInt(indiceAnioPublicacion)
                val precio = resultadoConsultaLectura.getDouble(indicePrecio)
                val disponible = resultadoConsultaLectura.getInt(indiceDisponible) == 1
                val bibliotecaNombre = resultadoConsultaLectura.getString(indiceBibliotecaNombre)

                // Crear objeto Libro con los datos obtenidos
                val libro = Libro(id, titulo, autor, anioPublicacion, precio, disponible, bibliotecaNombre)
                arregloRespuesta.add(libro)
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return arregloRespuesta
    }

}
