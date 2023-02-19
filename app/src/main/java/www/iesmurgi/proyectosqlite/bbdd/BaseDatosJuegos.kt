package www.iesmurgi.proyectosqlite.bbdd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosJuegos(contexto: Context):SQLiteOpenHelper(contexto,DATABASE,null,VERSION){
    companion object{
        const val VERSION=1
        const val DATABASE="juegosConsolasBD.sql"
        const val TABLA="juegos_tb"
    }

    override fun onCreate(bbdd: SQLiteDatabase?) {
        val q = "CREATE TABLE $TABLA(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "consola TEXT NOT NULL," +
                "imagen BLOB NOT NULL," +
                "titulo TEXT NOT NULL, " +
                "descripcion TEXT NOT NULL, "+
                "estrellas FLOAT NOT NULL,"+
                "jugado TEXT NOT NULL, " +
                "UNIQUE (titulo, consola))"

        bbdd?.execSQL(q)
    }

    override fun onUpgrade(bbdd: SQLiteDatabase?, p1: Int, p2: Int) {
        val q="DROP TABLE IF EXISTS $TABLA"
        bbdd?.execSQL(q)
        onCreate(bbdd)
    }
    //CRUD create, read, update, delete
    //Crear un registro


    fun create(juego: Juegos) :Long{
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put ("CONSOLA",juego.consola)
            put("IMAGEN", juego.imagen)
            put("TITULO", juego.titulo)
            put ("DESCRIPCION", juego.descripcion)
            put("ESTRELLAS", juego.estrellas)
            put("JUGADO", juego.jugado)

        }
        val cod=conexion.insert(TABLA, null, valores)
        conexion.close()
        return cod
    }
    @SuppressLint("Range")
    fun read(): MutableList<Juegos>{ //ver todos los registros
        val listaJuegos = mutableListOf<Juegos>()
        val conexion = this.readableDatabase
        val consulta="SELECT * FROM $TABLA ORDER BY estrellas DESC, titulo"

        try{
            val cursor = conexion.rawQuery(consulta, null)
            if(cursor.moveToFirst()){
                do{
                    val anime=Juegos(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("consola")),
                        cursor.getBlob(cursor.getColumnIndex("imagen")),
                        cursor.getString(cursor.getColumnIndex("titulo")),
                        cursor.getString(cursor.getColumnIndex("descripcion")),
                        cursor.getFloat(cursor.getColumnIndex("estrellas")),
                        cursor.getString(cursor.getColumnIndex("jugado"))
                    )
                    listaJuegos.add(anime)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return listaJuegos
    }

    fun update(juego: Juegos): Int{
        //val q="UPDATE $TABLA SET nombre='${usuario.nombre}', email='${usuario.email}' where id=${usuario.id}"
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("CONSOLA", juego.consola)
            put("IMAGEN", juego.imagen)
            put("TITULO", juego.titulo)
            put ("DESCRIPCION",juego.descripcion)
            put ("ESTRELLAS",juego.estrellas)
            put("JUGADO", juego.jugado)

        }
        val update = conexion.update(TABLA, valores, "titulo=? and categoria=?", arrayOf(juego.titulo, juego.consola))
        conexion.close()
        return  update
    }

    fun delete(id: Int?){
        val q="DELETE FROM $TABLA WHERE id=$id"
        val conexion= this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }


    //metodo para comprobar que el email es unico
    fun existeEmail(titulo: String, id: Int?): Boolean{
        val consulta = if(id==null) "SELECT id from $TABLA where titulo='$titulo'" else
            "SELECT id from $TABLA where titulo='$titulo' AND id!=$id"
        val conexion = this.readableDatabase
        var filas=0
        try{
            val cursor = conexion.rawQuery(consulta, null)
            filas =cursor.count
            cursor.close()
        }catch(e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return (filas!=0)
    }

    fun borrarTodo(){
        val q="DELETE FROM $TABLA"
        val conexion = this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }

}