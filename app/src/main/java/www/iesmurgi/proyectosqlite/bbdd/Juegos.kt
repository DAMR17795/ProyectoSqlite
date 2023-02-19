package www.iesmurgi.proyectosqlite.bbdd

data class Juegos(
    var id:Int?,
    var consola:String,
    var imagen: ByteArray,
    var titulo:String,
    var descripcion:String,
    var estrellas:Float,
    var jugado: String
):java.io.Serializable
