package www.iesmurgi.proyectosqlite.actividades

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import www.iesmurgi.proyectosqlite.R
import www.iesmurgi.proyectosqlite.databinding.DescripcionBinding

class ItemActivity:AppCompatActivity() {
    lateinit var binding: DescripcionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DescripcionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cogerDatos()
    }

    fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {

            binding.etTitulo.text = datos.getString("TITULO")
            binding.etCategoria.text = datos.getString("CONSOLA")
            binding.etVisto.text = datos.getString("JUGADO")

            binding.etEstrellas.rating = datos.getFloat("ESTRELLAS")

            binding.etDescripcion.text = datos.getString("DESCRIPCION")

            val byteArray = datos.getByteArray("IMAGEN") // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
            binding.etImg.setImageBitmap(bitmap)

            if (binding.etVisto.text.equals("Jugado")) {
                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.etVisto.setText("Estado: Jugado ✔️")
            }
            if (binding.etVisto.text.equals("No Jugado")) {
                binding.etVisto.setText("Estado: No Jugado ❌")
                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
            if (binding.etVisto.text.equals("Jugándolo")) {

                binding.etVisto.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.etVisto.setText("Estado: Jugándolo \uD83C\uDFAE")
            }

            if (binding.etCategoria.text.equals("SWITCH")) {
                binding.etCategoria.setText("Consola: SWITCH")
                binding.etCategoria.setTextColor(ContextCompat.getColor(this, R.color.rojo))
                binding.tvLogoDescripcion.setImageResource(R.drawable.logoswitch)
            } else if (binding.etCategoria.text.equals("PS5")) {
                binding.etCategoria.setTextColor(ContextCompat.getColor(this, R.color.azulTitulo))
                binding.etCategoria.setText("Consola: PS5")
                binding.tvLogoDescripcion.setImageResource(R.drawable.pslogo)
            } else if (binding.etCategoria.text.equals("XBOX/X")) {
                binding.etCategoria.setTextColor(ContextCompat.getColor(this, R.color.verde))
                binding.etCategoria.setText("Consola: XBOX/X")
                binding.tvLogoDescripcion.setImageResource(R.drawable.xboxlogo)
            }


        }


    }
}