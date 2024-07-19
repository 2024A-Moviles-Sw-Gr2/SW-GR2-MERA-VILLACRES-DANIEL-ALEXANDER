package com.example.deber02_damv.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.deber02_damv.Biblioteca
import com.example.deber02_damv.Libro

class SQLiteHelperLibro(
    contexto: Context? // this
): SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                CREATE TABLE LIBRO(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo VARCHAR(50),
                    autor VARCHAR(50),
                    anioPublicacion INTEGER,
                    precio DECIMAL(15, 2),
                    disponible BOOLEAN,
                    bibliotecaNombre VARCHAR(50),
                    FOREIGN KEY (bibliotecaNombre) REFERENCES Bibliotecas(nombre)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }
    override fun onUpgrade(
        p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun crearLibro(
        titulo: String,
        autor: String,
        anioPublicacion: Int,
        precio: Double,
        disponible: Boolean,
        bibliotecaNombre: String
    ):Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        valoresAGuardar.put("titulo", titulo)
        valoresAGuardar.put("autor", autor)
        valoresAGuardar.put("anioPublicacion", anioPublicacion)
        valoresAGuardar.put("precio", precio)
        valoresAGuardar.put("disponible", disponible)
        valoresAGuardar.put("bibliotecaNombre", bibliotecaNombre)
        val resultadoGuardar = basedatosEscritura.insert(
            "LIBRO", // nombre tabla
            null,
            valoresAGuardar // valores
        )
        basedatosEscritura.close()
        return if(resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarLibro(id:Int):Boolean{
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura.delete(
            "LIBRO",
            "id=?",
            parametrosConsultaDelete
        )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt()==-1) false else true
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

    fun consultarLibroPorID(id:Int):Libro?{
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
        if(existeAlMenosUno){
            do{
                // Obtener los índices de las columnas
                val indiceTitulo = resultadoConsultaLectura.getColumnIndex("titulo")
                val indiceAutor = resultadoConsultaLectura.getColumnIndex("autor")
                val indiceAnioPublicacion = resultadoConsultaLectura.getColumnIndex("anioPublicacion")
                val indicePrecio = resultadoConsultaLectura.getColumnIndex("precio")
                val indiceDisponible = resultadoConsultaLectura.getColumnIndex("disponible")
                val indiceBibliotecaNombre = resultadoConsultaLectura.getColumnIndex("bibliotecaNombre")

                // Obtener los valores de las columnas
                val titulo = resultadoConsultaLectura.getString(indiceTitulo)
                val autor = resultadoConsultaLectura.getString(indiceAutor)
                val anioPublicacion = resultadoConsultaLectura.getInt(indiceAnioPublicacion)
                val precio = resultadoConsultaLectura.getDouble(indicePrecio)
                val disponible = resultadoConsultaLectura.getInt(indiceDisponible) // SQLite almacena booleanos como 0 o 1
                val bibliotecaNombre = resultadoConsultaLectura.getString(indiceBibliotecaNombre)

                // Convertir esPublica en booleano
                val disponibleBoolean = if(disponible==1) true else false

                // Crear objeto Libro con los datos obtenidos
                val libro = Libro(id, titulo, autor, anioPublicacion, precio, disponibleBoolean, bibliotecaNombre)
                arregloRespuesta.add(libro)
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return if(arregloRespuesta.size>0) arregloRespuesta[0] else null
    }

    fun obtenerIDLibro(nombre:String):Int?{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM LIBRO WHERE NOMBRE = ?
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

    fun consultarListaLibro():List<Libro>{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM LIBRO 
        """.trimIndent()
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            null
        )

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        val arregloRespuesta = arrayListOf<Libro>()
        if(existeAlMenosUno){
            do{
                // Obtener los índices de las columnas
                val indiceId = resultadoConsultaLectura.getColumnIndex("id")
                val indiceTitulo = resultadoConsultaLectura.getColumnIndex("titulo")
                val indiceAutor = resultadoConsultaLectura.getColumnIndex("autor")
                val indiceAnioPublicacion = resultadoConsultaLectura.getColumnIndex("anioPublicacion")
                val indicePrecio = resultadoConsultaLectura.getColumnIndex("precio")
                val indiceDisponible = resultadoConsultaLectura.getColumnIndex("disponible")
                val indiceBibliotecaNombre = resultadoConsultaLectura.getColumnIndex("bibliotecaNombre")

                // Obtener los valores de las columnas
                val id = resultadoConsultaLectura.getInt(indiceId)
                val titulo = resultadoConsultaLectura.getString(indiceTitulo)
                val autor = resultadoConsultaLectura.getString(indiceAutor)
                val anioPublicacion = resultadoConsultaLectura.getInt(indiceAnioPublicacion)
                val precio = resultadoConsultaLectura.getDouble(indicePrecio)
                val disponible = resultadoConsultaLectura.getInt(indiceDisponible) // SQLite almacena booleanos como 0 o 1
                val bibliotecaNombre = resultadoConsultaLectura.getString(indiceBibliotecaNombre)

                // Convertir esPublica en booleano
                val disponibleBoolean = if(disponible==1) true else false

                // Crear objeto Libro con los datos obtenidos
                val libro = Libro(id, titulo, autor, anioPublicacion, precio, disponibleBoolean, bibliotecaNombre)
                arregloRespuesta.add(libro)
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return arregloRespuesta
    }
}