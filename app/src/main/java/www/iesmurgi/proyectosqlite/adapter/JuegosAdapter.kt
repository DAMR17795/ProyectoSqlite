package www.iesmurgi.proyectosqlite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.proyectosqlite.R
import www.iesmurgi.proyectosqlite.bbdd.Juegos


class JuegosAdapter(
    private var lista: MutableList<Juegos>,
    private val onItemDelete:(Int)->Unit,
    private val onItemUpdate:(Juegos) -> Unit
) : RecyclerView.Adapter<JuegosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegosViewHolder { //2.
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return JuegosViewHolder(v)
    }

    fun setList(lista2:MutableList<Juegos>){
        this.lista = lista2
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: JuegosViewHolder, position: Int) {
        holder.inflar(lista[position], onItemDelete, onItemUpdate)
    }

    override fun getItemCount() = lista.size  //1

    fun deleteItem(i: Int) {

        lista.removeAt(i)
        notifyItemRemoved(i)

    }


}