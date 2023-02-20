package www.iesmurgi.proyectosqlite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.proyectosqlite.actividades.Crear_Modificar
import www.iesmurgi.proyectosqlite.adapter.JuegosAdapter
import www.iesmurgi.proyectosqlite.bbdd.BaseDatosJuegos
import www.iesmurgi.proyectosqlite.bbdd.Juegos
import www.iesmurgi.proyectosqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView

    private lateinit var binding: ActivityMainBinding
    private lateinit var conexion: BaseDatosJuegos
    private lateinit var miAdapter: JuegosAdapter
    private var lista = mutableListOf<Juegos>()

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner

    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvMain = findViewById(R.id.rvMain)


        conexion = BaseDatosJuegos(this)
        searchView = binding.searchview
        searchView.clearFocus()

        spinner1 = binding.spVisto
        spinner2 = binding.spCategoria
        spinners()

        listeners()
        setRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    fun mostrarAbout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.acerca).setIcon(R.drawable.acercadeicon).setMessage(getString(R.string.about) + "\n\nDaniel Alejandro Martín Romero - 2ºDAM")
        //Mostramos el dialogo
        val dialog = builder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.crear -> abrirCrear()
            R.id.borrar -> borrar()
            R.id.cerrar -> mostrarAbout()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    fun borrar(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Borrar contenido")
        builder.setMessage("¿Estás seguro de borrar todos los datos?")
        builder.setIcon(R.drawable.ic_delete)

        builder.setPositiveButton("Confirmar") { dialog, which ->
            conexion.borrarTodo()
            lista.clear()
            miAdapter.notifyDataSetChanged()
            binding.tvNo.visibility = View.VISIBLE
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        val buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.verde))

        val buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.rojo))


    }

    fun abrirCrear(){

        startActivity(Intent(this, Crear_Modificar::class.java))
    }

    fun spinners() {

        var listaVisto = listOf<String>("TODO", "JUGADO", "NO JUGADO", "JUGÁNDOLO")
        var listaCategoria = listOf<String>("TODO", "PS5", "SWITCH", "XBOX/X")
        spinner1 = binding.spVisto
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaVisto)
        spinner1.adapter = adapter



        spinner2 = binding.spCategoria
        val adapter2 =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaCategoria)
        spinner2.adapter = adapter2

    }

    fun listeners() {

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, Crear_Modificar::class.java))
        }

        binding.button.setOnClickListener {


            if (filterList(
                    searchView.query.toString(),
                    spinner1.selectedItem.toString(),
                    spinner2.selectedItem.toString()
                ) == 0
            ) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("No hay videojuegos con esa configuracion.")
                builder.setPositiveButton("OK") { dialog, which ->
                    //Acción a realizar cuando se presiona el botón OK
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submit event here
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val spiner1Sel = spinner1.selectedItem.toString()
                val spiner2Sel = spinner2.selectedItem.toString()
                filterList(newText, spiner1Sel, spiner2Sel)



                return true
            }
        })


    }


    private fun filterList(text: String?, textoSpinner1: String, textoSpinner2: String): Int {

        var listaCopia = mutableListOf<Juegos>()

        for (anime in lista) {

            if (textoSpinner1.equals("TODO") && textoSpinner2.equals("TODO")) {
                if (anime.titulo.toLowerCase().contains(text.toString().toLowerCase())) {
                    listaCopia.add(anime)
                }
            } else if (!textoSpinner1.equals("TODO") && textoSpinner2.equals("TODO")) {

                if (anime.titulo.toLowerCase()
                        .contains(text.toString().toLowerCase()) && (anime.jugado.toLowerCase()
                        .equals(textoSpinner1.toLowerCase()))
                ) {
                    listaCopia.add(anime)
                }

            } else if (textoSpinner1.equals("TODO") && !textoSpinner2.equals("TODO")) {

                if (anime.titulo.toLowerCase()
                        .contains(text.toString().toLowerCase()) && (anime.consola.toLowerCase()
                        .equals(textoSpinner2.toLowerCase()))
                ) {
                    listaCopia.add(anime)
                }

            } else if (!textoSpinner1.equals("TODO") && !textoSpinner2.equals("TODO")) {

                if (anime.titulo.toLowerCase()
                        .contains(text.toString().toLowerCase()) && anime.consola.toLowerCase()
                        .equals(textoSpinner2.toLowerCase()) && anime.jugado.toLowerCase()
                        .equals(textoSpinner1.toLowerCase())
                ) {
                    listaCopia.add(anime)
                }

            }

        }
        if (listaCopia.isEmpty()) {

            return 0


        } else {

            miAdapter.setList(listaCopia)
            return 1

        }

    }

    override fun onResume() {
        setRecycler()
        searchView.setQuery("", false)
        super.onResume()
    }

    private fun setRecycler() {
        lista = conexion.read()
        binding.tvNo.visibility = View.INVISIBLE
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
            return
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager
        miAdapter = JuegosAdapter(lista, { onItemDelete(it) }) { anime -> onItemUpdate(anime) }
        binding.rvMain.adapter = miAdapter

    }


    private fun onItemDelete(position: Int) {
        val usuario = lista[position]
        conexion.delete(usuario.id)
        //borramos de la lista e indicamos al adapter que hemos
        //eliminado un registro
        lista.removeAt(position)
        if (lista.size == 0) {
            binding.tvNo.visibility = View.VISIBLE
        }
        miAdapter.notifyItemRemoved(position)
    }

    private fun onItemUpdate(anime: Juegos) {
        val i = Intent(this, Crear_Modificar::class.java).apply {
            putExtra("ANIMES", anime)
        }
        startActivity(i)
    }
}