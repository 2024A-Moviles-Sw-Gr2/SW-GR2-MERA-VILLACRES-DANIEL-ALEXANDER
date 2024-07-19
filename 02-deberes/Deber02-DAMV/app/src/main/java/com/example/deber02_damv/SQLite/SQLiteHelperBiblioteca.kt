package com.example.deber02_damv.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.deber02_damv.Biblioteca
import java.util.Date

class SQLiteHelperBiblioteca(
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
                CREATE TABLE BIBLIOTECA(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    ubicacion VARCHAR(50),
                    fechaCreacion DATE,
                    presupuestoAnual DECIMAL(15, 2),
                    esPublica BOOLEAN
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }
    override fun onUpgrade(
        p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun crearBiblioteca(
        nombre: String,
        ubicacion: String,
        fechaCreacion: Date,
        presupuestoAnual: Double,
        esPublica: Boolean
    ):Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        // Convertir fechaCreacion a timestamp en milisegundos
        val fechaTimestamp = fechaCreacion.time

        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("ubicacion", ubicacion)
        valoresAGuardar.put("fechaCreacion", fechaTimestamp)
        valoresAGuardar.put("presupuestoAnual", presupuestoAnual)
        valoresAGuardar.put("esPublica", esPublica)
        val resultadoGuardar = basedatosEscritura.insert(
                "BIBLIOTECA", // nombre tabla
                null,
                valoresAGuardar // valores
            )
        basedatosEscritura.close()
        return if(resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarBiblioteca(id:Int):Boolean{
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura.delete(
                "BIBLIOTECA",
                "id=?",
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt()==-1) false else true
    }

    fun actualizarBiblioteca(
        id: Int,
        nombre: String,
        ubicacion: String,
        fechaCreacion: Date,
        presupuestoAnual: Double,
        esPublica: Boolean
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()

        // Convertir fechaCreacion a timestamp en milisegundos
        val fechaTimestamp = fechaCreacion.time

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("ubicacion", ubicacion)
        valoresAActualizar.put("fechaCreacion", fechaTimestamp)
        valoresAActualizar.put("presupuestoAnual", presupuestoAnual)
        valoresAActualizar.put("esPublica", esPublica)

        val parametrosConsultaActualizar = arrayOf( id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "BIBLIOTECA",
                valoresAActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if(resultadoActualizacion.toInt()==-1) false else true
    }

    fun consultarBibliotecaPorNombre(nombre:String):Biblioteca?{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BIBLIOTECA WHERE NOMBRE = ?
        """.trimIndent()
        val arregloParametrosConsultaLectura = arrayOf(nombre.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
                scriptConsultaLectura,
                arregloParametrosConsultaLectura
            )

        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        val arregloRespuesta = arrayListOf<Biblioteca>()
        if(existeAlMenosUno){
            do{
                // Obtener los índices de las columnas
                val indiceNombre = resultadoConsultaLectura.getColumnIndex("nombre")
                val indiceUbicacion = resultadoConsultaLectura.getColumnIndex("ubicacion")
                val indiceFechaCreacion = resultadoConsultaLectura.getColumnIndex("fechaCreacion")
                val indicePresupuestoAnual = resultadoConsultaLectura.getColumnIndex("presupuestoAnual")
                val indiceEsPublica = resultadoConsultaLectura.getColumnIndex("esPublica")

                // Obtener los valores de las columnas
                val nombreBiblioteca = resultadoConsultaLectura.getString(indiceNombre)
                val ubicacion = resultadoConsultaLectura.getString(indiceUbicacion)
                val fechaCreacionTimestamp = resultadoConsultaLectura.getLong(indiceFechaCreacion)
                val presupuestoAnual = resultadoConsultaLectura.getDouble(indicePresupuestoAnual)
                val esPublica = resultadoConsultaLectura.getInt(indiceEsPublica) // SQLite almacena booleanos como 0 o 1

                // Convertir timestamp a Date
                val fechaCreacion = Date(fechaCreacionTimestamp)

                // Convertir esPublica en booleano
                val esPublicaBoolean = if(esPublica==1) true else false

                // Crear objeto Biblioteca con los datos obtenidos
                val biblioteca = Biblioteca(nombreBiblioteca, ubicacion, fechaCreacion, presupuestoAnual, esPublicaBoolean)
                arregloRespuesta.add(biblioteca)
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return if(arregloRespuesta.size>0) arregloRespuesta[0] else null
    }

    fun obtenerIDBiblioteca(nombre:String):Int?{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM BIBLIOTECA WHERE NOMBRE = ?
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

    fun consultarListaBiblioteca():List<Biblioteca>{
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
        if(existeAlMenosUno){
            do{
                // Obtener los índices de las columnas
                val indiceNombre = resultadoConsultaLectura.getColumnIndex("nombre")
                val indiceUbicacion = resultadoConsultaLectura.getColumnIndex("ubicacion")
                val indiceFechaCreacion = resultadoConsultaLectura.getColumnIndex("fechaCreacion")
                val indicePresupuestoAnual = resultadoConsultaLectura.getColumnIndex("presupuestoAnual")
                val indiceEsPublica = resultadoConsultaLectura.getColumnIndex("esPublica")

                // Obtener los valores de las columnas
                val nombreBiblioteca = resultadoConsultaLectura.getString(indiceNombre)
                val ubicacion = resultadoConsultaLectura.getString(indiceUbicacion)
                val fechaCreacionTimestamp = resultadoConsultaLectura.getLong(indiceFechaCreacion)
                val presupuestoAnual = resultadoConsultaLectura.getDouble(indicePresupuestoAnual)
                val esPublica = resultadoConsultaLectura.getInt(indiceEsPublica) // SQLite almacena booleanos como 0 o 1

                // Convertir timestamp a Date
                val fechaCreacion = Date(fechaCreacionTimestamp)

                // Convertir esPublica en booleano
                val esPublicaBoolean = if(esPublica==1) true else false

                // Crear objeto Biblioteca con los datos obtenidos
                val biblioteca = Biblioteca(nombreBiblioteca, ubicacion, fechaCreacion, presupuestoAnual, esPublicaBoolean)
                arregloRespuesta.add(biblioteca)
            }while(resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return arregloRespuesta
    }
}