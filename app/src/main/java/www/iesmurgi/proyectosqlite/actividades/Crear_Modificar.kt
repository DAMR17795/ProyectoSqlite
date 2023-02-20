package www.iesmurgi.proyectosqlite.actividades

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import www.iesmurgi.proyectosqlite.R
import www.iesmurgi.proyectosqlite.bbdd.BaseDatosJuegos
import www.iesmurgi.proyectosqlite.bbdd.Juegos
import www.iesmurgi.proyectosqlite.databinding.CrearModificarBinding
import java.io.ByteArrayOutputStream

class Crear_Modificar:AppCompatActivity() {
    var titulo = ""
    var descripcion = ""
    var categoria = ""
    var estrellas = 0.0f
    var id: Int? = null
    var visto = ""
    var editar = false

    lateinit var binding: CrearModificarBinding
    lateinit var conexion: BaseDatosJuegos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CrearModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion = BaseDatosJuegos(this)
        binding.imageView.setImageResource(R.drawable.camara)
        binding.imageView.setTag(R.drawable.camara)

        datosNoVacios()
        autocomplete()
        cogerDatos()
        setListeners()
    }

    companion object {
        const val PICK_IMAGE_REQUEST_GALERIA=20
        const val PICK_IMAGE_REQUEST_CAMERA=21
    }

    private fun abrirFoto() {

     var options = listOf(resources.getString(R.string.foto), resources.getString(R.string.galeria))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.elegir))
        builder.setItems(options.toTypedArray()){ _, item ->
            when {
                options[item] == resources.getString(R.string.foto) -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, PICK_IMAGE_REQUEST_CAMERA)
                }
                options[item] == resources.getString(R.string.galeria) -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, PICK_IMAGE_REQUEST_GALERIA)
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_GALERIA && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            binding.imageView.setImageURI(imageUri)
            binding.imageView.setTag(imageUri)

        } else if ( requestCode == PICK_IMAGE_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val imagen = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imagen)
            binding.imageView.setTag(imagen)
            binding.imageView.setScaleX(1.0F)
            binding.imageView.setScaleY(1.0F)
        }
    }


    private fun setListeners() {
        binding.imageView.setOnClickListener {
            abrirFoto()
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }


    fun datosNoVacios() {
        binding.etTitulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()) {

                    binding.Titulo.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()) {

                    binding.Descripcion.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    fun autocomplete() {
        var listaVisto = listOf<String>(resources.getString(R.string.jugado), resources.getString(R.string.no_jugado), resources.getString(R.string.jugandolo))
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaVisto)
        binding.autocompleteVisto.setAdapter(adapter)
        binding.autocompleteVisto.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            binding.autocompleteVisto.setText(selected)
        }

        binding.autocompleteVisto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false
                for (item in listaVisto) {
                    if (item == input) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    binding.Visto.helperText = "Error de formato"
                    binding.Visto.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.rojo)
                } else {
                    binding.Visto.helperText = ""
                    binding.Visto.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.verde)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        var listaCategoria = listOf<String>("XBOX/X", "PS5", "SWITCH")
        val adapter2 =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaCategoria)
        binding.autocompleteCategoria.setAdapter(adapter2)
        binding.autocompleteCategoria.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position) as String
            binding.autocompleteCategoria.setText(selected)

        }

        binding.autocompleteCategoria.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false
                for (item in listaCategoria) {
                    if (item == input) {
                        if (item.equals("SWITCH")) {
                            val imagen = binding.logoConsola
                            imagen.setImageResource(R.drawable.logoswitch)
                        } else if (item.equals("PS5")) {
                            val imagen = binding.logoConsola
                            imagen.setImageResource(R.drawable.pslogo)
                        } else if (item.equals("XBOX/X")) {
                            val imagen = binding.logoConsola
                            imagen.setImageResource(R.drawable.xboxlogo)
                        }
                        found = true
                        break

                    }
                }
                if (!found) {
                    binding.Categoria.helperText = resources.getString(R.string.no_existe)
                    binding.Categoria.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.rojo)
                } else {
                    binding.Categoria.helperText = ""
                    binding.Categoria.boxStrokeColor =
                        ContextCompat.getColor(applicationContext, R.color.verde)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    private fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            editar = true
            binding.btnCrear.text = resources.getString(R.string.editar) + "  ✏️"
            binding.textView.text = resources.getString(R.string.modificar)
            val juego = datos.getSerializable("JUEGOS") as Juegos
            id = juego.id
            binding.etTitulo.setText(juego.titulo)
            binding.etDescripcion.setText(juego.descripcion)


            binding.autocompleteCategoria.setText(juego.consola)

            binding.autocompleteVisto.setText(juego.jugado)


            binding.etStar.rating = juego.estrellas

            val byteArray = juego.imagen // tu array de bytes

            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imageView.setImageBitmap(bitmap)
            //Cuidado
            binding.imageView.setTag(bitmap)

        }
    }

    private fun crearRegistro() {

        titulo = binding.etTitulo.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        categoria = binding.autocompleteCategoria.text.toString().trim()
        estrellas = binding.etStar.rating

        if (binding.imageView.getTag() == R.drawable.camara) {
            binding.imageView.setImageResource(R.drawable.nophoto)
            binding.imageView.setTag(R.drawable.nophoto)
        }


        visto = binding.autocompleteVisto.text.toString().trim()


        val imageView: ImageView = binding.imageView
        if (imageView.drawable != null) {
            val drawable = imageView.getDrawable()
            val bitmap = (drawable as BitmapDrawable).bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            var listaCategoria = listOf<String>("XBOX/X", "PS5", "SWITCH")
            var listaVisto = listOf<String>(resources.getString(R.string.jugado), resources.getString(R.string.no_jugado), resources.getString(R.string.jugandolo))

            var txt = binding.autocompleteCategoria.text
            var foundCategoria = false
            for (item in listaCategoria) {
                if (item.equals(txt)) {
                    foundCategoria = true
                    break
                }

            }

            var txt2 = binding.autocompleteVisto.text
            var foundVisto = false
            for (item in listaVisto) {
                if (item.equals(txt2)) {
                    foundVisto = true
                    break
                }
            }

            if (binding.etTitulo.text!!.isEmpty()) {

                binding.Titulo.error = resources.getString(R.string.titulo_vacio)


            } else if (binding.etDescripcion.text!!.isEmpty()) {

                binding.Descripcion.error = resources.getString(R.string.descripcion_vacia)

            } else if (binding.autocompleteVisto.text.isEmpty()) {
                    //categoria, byteArray, titulo, descripcion, estrellas, visto

                binding.Visto.error = resources.getString(R.string.campo_vacio)

            } else if (binding.autocompleteCategoria.text.isEmpty()) {
                binding.Visto.error = resources.getString(R.string.campo_vacio)
            } else  {
                if (!editar) {
                    val juego =
                        Juegos(1, categoria, byteArray, titulo, descripcion, estrellas, visto)
                    if (conexion.create(juego) > -1) {
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.registro),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                } else {
                    val juego =
                        Juegos(1, categoria, byteArray, titulo, descripcion, estrellas, visto)
                    if (conexion.update(juego) > -1) {
                        finish()
                    } else {
                        Toast.makeText(this, resources.getString(R.string.registro), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        } else {
            println("error")
        }
    }

}