package www.iesmurgi.proyectosqlite.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.proyectosqlite.R
import www.iesmurgi.proyectosqlite.actividades.ItemActivity
import www.iesmurgi.proyectosqlite.bbdd.Juegos
import www.iesmurgi.proyectosqlite.databinding.ItemBinding


class JuegosViewHolder(vista: View) : RecyclerView.ViewHolder(vista) {
    //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding = ItemBinding.bind(vista)

    fun inflar(
        juego: Juegos,
        onItemDelete: (Int) -> Unit,
        onItemUpdate: (Juegos) -> Unit
    ) {

        miBinding.btnBorrar.setOnClickListener {
            onItemDelete(adapterPosition)
        }

        miBinding.btnEditar.setOnClickListener {
            onItemUpdate(juego)
        }

        itemView.setOnClickListener {

            val intent = Intent(itemView.context, ItemActivity::class.java)
            intent.putExtra("CONSOLA", juego.consola)
            intent.putExtra("IMAGEN", juego.imagen)
            intent.putExtra("TITULO", juego.titulo)
            intent.putExtra("DESCRIPCION", juego.descripcion)
            intent.putExtra("ESTRELLAS", juego.estrellas)
            intent.putExtra("JUGADO", juego.jugado)

            ContextCompat.startActivity(itemView.context, intent, null)

        }

        if (juego.titulo.length < 25) {

            miBinding.tvTitle.text = juego.titulo

        } else {

            miBinding.tvTitle.text = juego.titulo.take(23) + " ..."
            miBinding.tvTitle.maxLines = 2

        }

        miBinding.tvCategory.text = juego.consola
        val descripcion = juego.descripcion
        val descripcionLimited = descripcion.split(" ").take(12).joinToString(" ")
        miBinding.tvDescription.text = descripcionLimited + " ..."
        miBinding.tvNumEstrellas.text = juego.estrellas.toString()
        miBinding.tvStars.rating = juego.estrellas
        val byteArray = juego.imagen // tu array de bytes
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        miBinding.ivMain.setImageBitmap(bitmap)
        miBinding.tvVisto.text = juego.jugado

        if (juego.jugado.equals("Jugado")) {

            miBinding.tvVisto.setTextColor(ContextCompat.getColor(itemView.context, R.color.verde))
        }
        if (juego.jugado.equals("No Jugado")) {

            miBinding.tvVisto.setTextColor(ContextCompat.getColor(itemView.context, R.color.rojo))
        }
        if (juego.jugado.equals("Jugándolo")) {

            miBinding.tvVisto.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.amarillo
                )
            )
        }

        var numStar = miBinding.tvStars.rating

        if (numStar >= 4.0f) {

            miBinding.tvNumEstrellas.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.verde
                )
            )

        } else if (numStar >= 1.5f) {

            miBinding.tvNumEstrellas.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.amarillo
                )
            )

        } else {


            miBinding.tvNumEstrellas.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.rojo
                )
            )


        }


    }


}